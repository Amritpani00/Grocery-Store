import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'

interface Product {
	id: string
	name: string
	description: string
	price: number
	imageUrl: string
	inStock: boolean
}

export default function Products() {
	const [products, setProducts] = useState<Product[]>([])
	const [q, setQ] = useState('')
	const [msg, setMsg] = useState('')
	const [searchParams] = useSearchParams()
	const categoryId = searchParams.get('categoryId')

	useEffect(() => {
		const ctrl = new AbortController()
		const params = new URLSearchParams()
		if (q) params.set('q', q)
		if (categoryId) params.set('categoryId', categoryId)
		fetch(`/api/products?${params.toString()}`, { signal: ctrl.signal })
			.then(r => r.json())
			.then(page => setProducts(page.content || []))
			.catch(() => {})
		return () => ctrl.abort()
	}, [q, categoryId])

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
		<div className="space-y-6">
			<div className="flex items-center gap-2">
				<input value={q} onChange={e => setQ(e.target.value)} placeholder="Search products..." className="border rounded-md px-3 py-2 w-full" />
			</div>
			{msg && <div className="text-sm text-emerald-700">{msg}</div>}
			<div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
				{products.map(p => (
					<div key={p.id} className="bg-white rounded-xl border overflow-hidden">
						<div className="aspect-square bg-gray-100" />
						<div className="p-3">
							<div className="font-medium truncate">{p.name}</div>
							<div className="text-sm text-gray-500 truncate">{p.description}</div>
							<div className="mt-2 flex items-center justify-between">
								<span className="font-semibold">${p.price.toFixed(2)}</span>
								<button onClick={()=>addToCart(p.id)} className="px-3 py-1.5 bg-emerald-600 text-white rounded-md text-sm">Add</button>
							</div>
						</div>
					</div>
				))}
			</div>
		</div>
	)
}