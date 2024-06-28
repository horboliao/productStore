import {Category, CategoryWithProductsCount, NewCategory} from "@/lib/types";
import {getProductByCategory} from "@/actions/products";
import {useSession} from "next-auth/react";
import {auth} from "@/auth";

export async function getCategories (): Promise<Category[]> {
    const session = await auth();
    console.log(session?.user?.token);
    try {
        const response = await fetch('http://localhost:8000/api/group', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                headers: {
                    'Authorization': `${session?.user?.token}`
                }
            },
            next: { revalidate: 0}
        });

        if (response.ok) {
            const groups = await response.json();
            console.log('Groups:', groups);
            return groups;
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

export async function getCategoriesWithProductsCount(): Promise<CategoryWithProductsCount[]> {
    const categories: Category[] = await getCategories();

    if (!categories) {
        return [];
    }

    const categoriesWithProductCountPromises = categories.map(async (category) => {
        const products = await getProductByCategory(category.id);
        return {
            ...category,
            amount: products.length
        };
    });

    return Promise.all(categoriesWithProductCountPromises);
}

export async function createCategory (data: NewCategory): Promise<Category> {
    try {
        const response = await fetch('http://localhost:8080/api/group', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            const result = await response.text();
            console.log('Created Group:', result);
        } else {
            console.error('Error:', response.statusText);
        }

    } catch (error) {
        console.error('Fetch error:', error);
    }
}

export async function updateCategory (id: number|string,data: NewCategory): Promise<Category> {
    try {
        const response = await fetch(`http://localhost:8080/api/group/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            console.log('Group updated');
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

export async function deleteCategory (id: number|string): Promise<Category> {
    try {
        const response = await fetch(`http://localhost:8080/api/group/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            console.log('Group deleted');
        } else {
            console.error('Error:', response.statusText);
        }
    } catch (error) {
        console.error('Fetch error:', error);
    }
}