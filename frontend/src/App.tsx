import { Link, Outlet } from 'react-router-dom'

function App() {
	const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null
	return (
		<div className="min-h-dvh bg-gray-50 text-gray-900">
			<header className="sticky top-0 z-10 bg-white border-b">
				<div className="mx-auto max-w-6xl px-4 h-16 flex items-center justify-between">
					<Link to="/" className="text-xl font-bold text-emerald-600">GrocerEase</Link>
					<nav className="flex items-center gap-4">
						<Link to="/products" className="hover:text-emerald-600">Products</Link>
						<Link to="/cart" className="hover:text-emerald-600">Cart</Link>
						{token ? (
							<>
								<Link to="/orders" className="hover:text-emerald-600">Orders</Link>
								<button className="hover:text-emerald-600" onClick={()=>{ localStorage.removeItem('token'); location.href='/' }}>Logout</button>
							</>
						) : (
							<Link to="/login" className="hover:text-emerald-600">Login</Link>
						)}
					</nav>
				</div>
			</header>
			<main className="mx-auto max-w-6xl px-4 py-8">
				<Outlet />
			</main>
		</div>
	)
}

export default App
