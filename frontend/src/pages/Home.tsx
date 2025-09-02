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

interface Category {
	id: string;
	name: string;
}

interface Banner {
	id: string;
	title: string;
	subtitle: string;
	imageUrl: string;
	link: string;
}

export default function Home() {
	const [discounted, setDiscounted] = useState<Product[]>([])
	const [newProducts, setNewProducts] = useState<Product[]>([])
	const [topSellers, setTopSellers] = useState<Product[]>([])
	const [categories, setCategories] = useState<Category[]>([])
	const [banners, setBanners] = useState<Banner[]>([])
	const [currentBanner, setCurrentBanner] = useState(0)
	const [msg, setMsg] = useState('')

	useEffect(() => {
		fetch('/api/products/discounted').then(res => res.json()).then(setDiscounted)
		fetch('/api/products/new').then(res => res.json()).then(setNewProducts)
		fetch('/api/products/top-seller').then(res => res.json()).then(setTopSellers)
		fetch('/api/categories').then(res => res.json()).then(setCategories)
		fetch('/api/banners').then(res => res.json()).then(setBanners)
	}, [])

	useEffect(() => {
		if (banners.length > 1) {
			const interval = setInterval(() => {
				setCurrentBanner(prev => (prev + 1) % banners.length)
			}, 5000)
			return () => clearInterval(interval)
		}
	}, [banners.length])

	async function addToCart(productId: string) {
		const token = localStorage.getItem('token')
		if (!token) { location.href = '/login'; return }
		setMsg('')
		const res = await fetch('/api/cart/add', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
			body: JSON.stringify({ productId, quantity: 1 }),
		})
		if (!res.ok) {
			setMsg('Failed to add to cart')
			return
		}
		setMsg('Added to cart')
	}

	return (
		<div className="space-y-10">
			{msg && <div className="text-sm text-emerald-700">{msg}</div>}
			{banners.length > 0 && (
				<section
					className="bg-emerald-600 text-white rounded-2xl p-10 bg-cover bg-center"
					style={{ backgroundImage: `url(${banners[currentBanner].imageUrl})` }}
				>
					<h1 className="text-3xl font-bold">{banners[currentBanner].title}</h1>
					<p className="mt-2 text-emerald-100">{banners[currentBanner].subtitle}</p>
					<div className="mt-6">
						<Link to={banners[currentBanner].link} className="px-4 py-2 bg-white text-emerald-700 rounded-md font-medium">Shop now</Link>
					</div>
				</section>
			)}

			{discounted.length > 0 && (
				<section>
					<div className="flex justify-between items-center mb-4">
						<h2 className="text-2xl font-bold">Festival in 10% discount</h2>
						<div className="flex gap-2">
							<button
								onClick={() => document.getElementById('discounted-products')?.scrollBy({ left: -300, behavior: 'smooth' })}
								className="bg-gray-200 rounded-full p-2 hover:bg-gray-300"
							>
								<svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
									<path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
								</svg>
							</button>
							<button
								onClick={() => document.getElementById('discounted-products')?.scrollBy({ left: 300, behavior: 'smooth' })}
								className="bg-gray-200 rounded-full p-2 hover:bg-gray-300"
							>
								<svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
									<path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
								</svg>
							</button>
						</div>
					</div>
					<div className="relative">
						<div id="discounted-products" className="flex gap-4 overflow-x-auto snap-x snap-mandatory py-4">
							{discounted.map(product => (
								<ProductCard key={product.id} product={product} onAddToCart={addToCart} />
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
							<ProductCard key={product.id} product={product} onAddToCart={addToCart} />
						))}
					</div>
				</section>
			)}

			{topSellers.length > 0 && (
				<section>
					<h2 className="text-2xl font-bold mb-4">Super Sellers</h2>
					<div className="flex gap-4 overflow-x-auto snap-x snap-mandatory py-4">
						{topSellers.map(product => (
							<ProductCard key={product.id} product={product} onAddToCart={addToCart} />
						))}
					</div>
				</section>
			)}

			{categories.length > 0 && (
				<section>
					<h2 className="text-2xl font-bold mb-4">Shop by Category</h2>
					<div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
						{categories.map(category => (
							<Link key={category.id} to={`/products?categoryId=${category.id}`} className="bg-gray-100 p-4 rounded-lg text-center font-medium hover:bg-gray-200">
								{category.name}
							</Link>
						))}
					</div>
				</section>
			)}
		</div>
	)
}

function ProductCard({ product, onAddToCart }: { product: Product, onAddToCart: (id: string) => void }) {
	return (
		<div className="bg-white border rounded-lg overflow-hidden flex-shrink-0 snap-center" style={{ width: '200px' }}>
			<img src={product.imageUrl} alt={product.name} className="w-full h-32 object-cover" />
			<div className="p-3">
				<h3 className="font-bold">{product.name}</h3>
				<div className="flex items-center justify-between mt-1">
					<div className="flex items-center gap-2">
						<span className={`font-semibold ${product.discountPrice ? 'text-red-500' : 'text-gray-800'}`}>
							${product.discountPrice || product.price}
						</span>
						{product.discountPrice && (
							<span className="text-gray-500 line-through">${product.price}</span>
						)}
					</div>
					<button onClick={() => onAddToCart(product.id)} className="px-3 py-1.5 bg-emerald-600 text-white rounded-md text-sm">Add</button>
				</div>
			</div>
		</div>
	)
}