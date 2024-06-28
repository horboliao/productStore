'use client'
import React from 'react';
import {
    Button,
    Card, CardBody, CardFooter,
    CardHeader,
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownTrigger,
    useDisclosure
} from "@nextui-org/react";
import {EllipsisVertical} from "lucide-react";
import {Category, ProductWithCategory} from "@/lib/types";
import {Badge} from "@nextui-org/badge";
import ProductAmountForm from "@/app/ui/forms/product/product-amount-form";
import ProductForm from "@/app/ui/forms/product/product-form";
import DeleteProductForm from "@/app/ui/forms/product/delete-product-form";

interface ProductCardProps {
    product: ProductWithCategory;
    categories: Category[];
}
const ProductCard = ({product, categories}:ProductCardProps) => {
    const {isOpen, onOpen, onClose} = useDisclosure();
    const [modalType, setModalType] = React.useState<'edit' | 'delete' | 'increaseAmount' | 'decreaseAmount' | null>(null);

    const handleOpenModal = (type: 'edit' | 'delete'| 'increaseAmount' | 'decreaseAmount') => {
        setModalType(type);
        onOpen();
    };

    const handleCloseModal = () => {
        setModalType(null);
        onClose();
    };
    return (
        <>
            <Badge content={product.amount} color="primary">
                <Card className='w-full'>
                    <CardHeader className="justify-between">
                        <div>
                            <p className="text-xs text-default-400">{product.category.toUpperCase()}</p>
                            <h3 className='font-semibold'>{product.name}</h3>
                        </div>
                        <Dropdown>
                            <DropdownTrigger>
                                <Button
                                    variant="bordered"
                                    isIconOnly
                                    size='sm'
                                >
                                    <EllipsisVertical size={16}/>
                                </Button>
                            </DropdownTrigger>
                            <DropdownMenu
                                aria-label="Action event example"
                            >
                                <DropdownItem key="amount" onPress={() => handleOpenModal('increaseAmount')}>Increase amount</DropdownItem>
                                <DropdownItem key="amount" onPress={() => handleOpenModal('decreaseAmount')}>Decrease amount</DropdownItem>
                                <DropdownItem key="edit" onPress={() => handleOpenModal('edit')}>Edit product</DropdownItem>
                                <DropdownItem key="delete" className="text-danger" color="danger" onPress={() => handleOpenModal('delete')}>
                                    Delete product
                                </DropdownItem>
                            </DropdownMenu>
                        </Dropdown>
                    </CardHeader>
                    <CardBody className="px-3 py-0 text-small text-default-400">
                        <p>{product.description}</p>
                    </CardBody>
                    <CardFooter className="gap-3">
                        <div className="flex gap-1">
                            <p className="font-semibold text-default-400 text-small">{product.price}$</p>
                            <p className="text-default-400 text-small">per one</p>
                        </div>
                        <div className="flex gap-1">
                            <p className="font-semibold text-default-400 text-small">{Number((product.amount*product.price).toFixed(2))}$</p>
                            <p className=" text-default-400 text-small">total</p>
                        </div>
                    </CardFooter>
                </Card>
            </Badge>
            {modalType === 'increaseAmount' && (
                <ProductAmountForm product={product} isOpen={isOpen} onOpenChange={handleCloseModal} increaseAmount/>
            )}
            {modalType === 'decreaseAmount' && (
                <ProductAmountForm product={product} isOpen={isOpen} onOpenChange={handleCloseModal}/>
            )}
            {modalType === 'edit' && (
                <ProductForm product={product} categories={categories} isOpen={isOpen} onOpenChange={handleCloseModal}/>
            )}

            {modalType === 'delete' && (
                <DeleteProductForm product={product} isOpen={isOpen} onOpenChange={handleCloseModal} />
            )}
        </>
    );
};

export default ProductCard;