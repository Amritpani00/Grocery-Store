import { useState } from 'react'

export default function Auth() {
	const [mode, setMode] = useState<'login'|'register'>('login')
	const [email, setEmail] = useState('')
	const [password, setPassword] = useState('')
	const [fullName, setFullName] = useState('')
	const [error, setError] = useState('')

	async function submit(e: React.FormEvent) {
		e.preventDefault()
		setError('')
		const endpoint = mode === 'login' ? '/api/auth/login' : '/api/auth/register'
		type Body = { email: string, password: string, fullName?: string }
		const body: Body = { email, password }
		if (mode === 'register') body.fullName = fullName
		const res = await fetch(endpoint, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(body),
		})
		const data = await res.json()
		if (!res.ok) { setError(data.error || 'Failed'); return }
		localStorage.setItem('token', data.token)
		location.href = '/'
	}

	return (
		<div className="max-w-md mx-auto">
			<div className="flex gap-2 mb-4">
				<button className={`px-3 py-1.5 rounded ${mode==='login'?'bg-emerald-600 text-white':'bg-gray-100'}`} onClick={() => setMode('login')}>Login</button>
				<button className={`px-3 py-1.5 rounded ${mode==='register'?'bg-emerald-600 text-white':'bg-gray-100'}`} onClick={() => setMode('register')}>Register</button>
			</div>
			<form onSubmit={submit} className="space-y-3 bg-white p-4 rounded-xl border">
				{mode==='register' && (
					<input value={fullName} onChange={e=>setFullName(e.target.value)} placeholder="Full name" className="border rounded-md px-3 py-2 w-full" />
				)}
				<input value={email} onChange={e=>setEmail(e.target.value)} placeholder="Email" className="border rounded-md px-3 py-2 w-full" />
				<input type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="Password" className="border rounded-md px-3 py-2 w-full" />
				{error && <div className="text-red-600 text-sm">{error}</div>}
				<button className="w-full bg-emerald-600 text-white rounded-md py-2">{mode==='login'?'Login':'Create account'}</button>
			</form>
		</div>
	)
}