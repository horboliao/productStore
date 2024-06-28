'use server'
import {Category, NewProduct, Product} from "@/lib/types";
import {getCategories} from "@/actions/categories";

export async function getProductByCategory (id: number|string): Promise<Product[]> {
    try {
        const response = await fetch(`http://localhost:8080/api/goods-in-group/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const products = await response.json();
            console.log('Products in group:', products);
            return products;
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}
export async function getProducts (): Promise<Product[]> {
    let products = [];
    try {
        const response = await fetch('http://localhost:8000/api/good/good', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            products = await response.json();
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }

    return products.map(product => ({
        id: product.id,
        group_id: product.group_name,
        name: product.name,
        description: product.description,
        producer: product.producer,
        amount: product.amount,
        price: product.sum_price
    }));
}

export async function createProduct(product: NewProduct): Promise<void> {
    const convertedProduct = {
        name: product.name,
        price: product.price,
        amount: product.amount,
        description: product.description,
        producer: product.producer,
        group_id: product.group_id,
    };

    try {
        const response = await fetch('http://localhost:8000/api/good', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(convertedProduct),
        });

        if (response.ok) {
            const result = await response.text();
            console.log('Created Product:', result);
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

export async function updateProduct(id: number | string, product: NewProduct): Promise<void> {
    const updatedProduct = {
        name: product.name,
        price: product.price,
        amount: product.amount,
        description: product.description,
        producer: product.producer,
        group_id: product.group_id,
    };

    try {
        const response = await fetch(`http://localhost:8000/api/good/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedProduct),
        });

        if (response.ok) {
            console.log('Updated Product:', await response.text());
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

export async function deleteProduct(id: number | string): Promise<void> {
    try {
        const response = await fetch(`http://localhost:8000/api/good/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            console.log('Deleted Product');
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

