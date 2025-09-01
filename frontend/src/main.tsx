import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App'
import Home from './pages/Home'
import Products from './pages/Products'
import Auth from './pages/Auth'
import Cart from './pages/Cart'
import Orders from './pages/Orders'

const router = createBrowserRouter([
	{
		path: '/',
		element: <App />,
		children: [
			{ index: true, element: <Home /> },
			{ path: 'products', element: <Products /> },
			{ path: 'login', element: <Auth /> },
			{ path: 'cart', element: <Cart /> },
			{ path: 'orders', element: <Orders /> },
		],
	},
])

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<RouterProvider router={router} />
	</React.StrictMode>,
)
