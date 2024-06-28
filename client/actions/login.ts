"use server"
import {signIn} from "@/auth";
export async function login(login: string, password: string): Promise<{ token: any }> {
    let token;
    const params = new URLSearchParams();
    params.append('login', login);
    params.append('password', password);

    try {
        const response = await fetch(`http://localhost:8000/login?login=${login}&password=${password}`, {
            method: 'POST',
        });

        if (response.ok) {
            token = await response.text();
            console.log('Token:', token);
        } else if (response.status === 401) {
            console.error('Unauthorized: Invalid login or password');
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
    return {token: token};
}

export async function handleLogin(login:string, password: string) {
    return await signIn('credentials', {
        login,
        password,
        redirectTo: '/'
    })
}