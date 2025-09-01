import { useEffect, useState } from 'react'

interface CartItem { productId: string; quantity: number }
interface Cart { id: string; items: CartItem[] }

export default function CartPage() {
	const [cart, setCart] = useState<Cart | null>(null)
	useEffect(() => {
		const token = localStorage.getItem('token')
		if (!token) return
		fetch('/api/cart', { headers: { Authorization: `Bearer ${token}` }})
			.then(r=>r.json()).then(setCart).catch(()=>{})
	}, [])
	return (
		<div className="space-y-4">
			<h2 className="text-xl font-semibold">Your Cart</h2>
			{!cart ? <div>Login to view your cart.</div> : (
				<div className="space-y-2">
					{cart.items.length===0 && <div>Your cart is empty.</div>}
					{cart.items.map(i => (
						<div key={i.productId} className="flex items-center justify-between bg-white p-3 border rounded">
							<div>{i.productId}</div>
							<div className="font-medium">x{i.quantity}</div>
						</div>
					))}
					{cart.items.length>0 && (
						<form method="dialog" onSubmit={async (e)=>{e.preventDefault();
							const token = localStorage.getItem('token')
							const res = await fetch('/api/orders/checkout', { method:'POST', headers:{'Content-Type':'application/json', Authorization:`Bearer ${token}`}, body: JSON.stringify({ address:'Demo', paymentMethod:'COD'})})
							if(res.ok){ location.href='/orders' }
						}}>
							<button className="px-4 py-2 bg-emerald-600 text-white rounded">Checkout</button>
						</form>
					)}
				</div>
			)}
		</div>
	)
}