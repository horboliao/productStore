export interface NewCategory  {
    name: string;
    description: string;
}

export interface Category extends NewCategory{
    id: number;
}

export interface NewProduct {
    category: number;
    name: string;
    description: string;
    producer: string;
    amount: number;
    price: number;
}

export interface Product extends NewProduct{
    id: number;
}

export interface ProductWithCategory{
    id: number;
    category: Category;
    name: string;
    description: string;
    producer: string;
    amount: number;
    price: number;
}