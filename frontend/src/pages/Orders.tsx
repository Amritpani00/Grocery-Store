import { useEffect, useState } from 'react'

interface Order { id: string; status: string }

export default function Orders() {
	const [orders, setOrders] = useState<Order[]>([])
	const [statusUpdates, setStatusUpdates] = useState<Record<string, string>>({})
	useEffect(() => {
		const token = localStorage.getItem('token')
		if (!token) return
		fetch('/api/orders', { headers: { Authorization: `Bearer ${token}` }})
			.then(r=>r.json()).then(setOrders).catch(()=>{})
	}, [])

	function streamStatus(orderId: string) {
		const es = new EventSource(`/api/tracking/stream/${orderId}`)
		es.onmessage = (e) => {
			setStatusUpdates(prev => ({ ...prev, [orderId]: e.data }))
		}
		es.onerror = () => es.close()
	}

	return (
		<div className="space-y-4">
			<h2 className="text-xl font-semibold">Orders</h2>
			<div className="space-y-2">
				{orders.map(o => (
					<div key={o.id} className="bg-white border rounded p-3 flex items-center justify-between">
						<div>
							<div className="font-medium">Order #{o.id.slice(-6)}</div>
							<div className="text-sm text-gray-600">{statusUpdates[o.id] || o.status}</div>
						</div>
						<button className="px-3 py-1.5 text-sm bg-gray-100 rounded" onClick={()=>streamStatus(o.id)}>Track</button>
					</div>
				))}
			</div>
		</div>
	)
}