import { Link } from 'react-router-dom'
import { useEffect, useState } from 'react'

interface Product {
	id: string;
	name: string;
	description: string;
	price: number;
	discountPrice?: number;
	imageUrl: string;
}

export default function Home() {
	const [discounted, setDiscounted] = useState<Product[]>([])
	const [newProducts, setNewProducts] = useState<Product[]>([])
	const [topSellers, setTopSellers] = useState<Product[]>([])

	useEffect(() => {
		fetch('/api/products/discounted').then(res => res.json()).then(setDiscounted)
		fetch('/api/products/new').then(res => res.json()).then(setNewProducts)
		fetch('/api/products/top-seller').then(res => res.json()).then(setTopSellers)
	}, [])

	return (
		<div className="space-y-10">
			<section className="bg-emerald-600 text-white rounded-2xl p-10">
				<h1 className="text-3xl font-bold">Fresh groceries delivered to your door</h1>
				<p className="mt-2 text-emerald-100">Browse products, add to cart, checkout and track your delivery in real time.</p>
				<div className="mt-6">
					<Link to="/products" className="px-4 py-2 bg-white text-emerald-700 rounded-md font-medium">Shop now</Link>
				</div>
			</section>

			{discounted.length > 0 && (
				<section>
					<h2 className="text-2xl font-bold mb-4">Festival in 10% discount</h2>
					<div className="relative">
						<div className="flex gap-4 overflow-x-auto snap-x snap-mandatory py-4">
							{discounted.map(product => (
								<ProductCard key={product.id} product={product} />
							))}
						</div>
					</div>
				</section>
			)}

			{newProducts.length > 0 && (
				<section>
					<h2 className="text-2xl font-bold mb-4">New Launches</h2>
					<div className="flex gap-4 overflow-x-auto snap-x snap-mandatory py-4">
						{newProducts.map(product => (
							<ProductCard key={product.id} product={product} />
						))}
					</div>
				</section>
			)}

			{topSellers.length > 0 && (
				<section>
					<h2 className="text-2xl font-bold mb-4">Super Sellers</h2>
					<div className="flex gap-4 overflow-x-auto snap-x snap-mandatory py-4">
						{topSellers.map(product => (
							<ProductCard key={product.id} product={product} />
						))}
					</div>
				</section>
			)}
		</div>
	)
}

function ProductCard({ product }: { product: Product }) {
	return (
		<div className="bg-white border rounded-lg overflow-hidden flex-shrink-0 snap-center" style={{ width: '200px' }}>
			<img src={product.imageUrl} alt={product.name} className="w-full h-32 object-cover" />
			<div className="p-3">
				<h3 className="font-bold">{product.name}</h3>
				<div className="flex items-center gap-2 mt-1">
					<span className={`font-semibold ${product.discountPrice ? 'text-red-500' : 'text-gray-800'}`}>
						${product.discountPrice || product.price}
					</span>
					{product.discountPrice && (
						<span className="text-gray-500 line-through">${product.price}</span>
					)}
				</div>
			</div>
		</div>
	)
}