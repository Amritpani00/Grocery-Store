import { Link } from 'react-router-dom'

export default function Home() {
	return (
		<div className="space-y-10">
			<section className="bg-emerald-600 text-white rounded-2xl p-10">
				<h1 className="text-3xl font-bold">Fresh groceries delivered to your door</h1>
				<p className="mt-2 text-emerald-100">Browse products, add to cart, checkout and track your delivery in real time.</p>
				<div className="mt-6">
					<Link to="/products" className="px-4 py-2 bg-white text-emerald-700 rounded-md font-medium">Shop now</Link>
				</div>
			</section>
		</div>
	)
}