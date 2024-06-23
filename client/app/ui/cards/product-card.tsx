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
import {ProductWithCategory} from "@/lib/types";
import {Badge} from "@nextui-org/badge";

interface ProductCardProps {
    product: ProductWithCategory;
}
const ProductCard = ({product}:ProductCardProps) => {
    const {isOpen, onOpen, onOpenChange, onClose} = useDisclosure();
    const [modalType, setModalType] = React.useState<'edit' | 'delete' | null>(null);

    const handleOpenModal = (type: 'edit' | 'delete') => {
        setModalType(type);
        onOpen();
    };

    const handleCloseModal = () => {
        setModalType(null);
        onClose();
    };
    return (
        <>
            <Badge content={product.amount} color="primary" size='sm'>
                <Card>
                    <CardHeader className="justify-between">
                        <div>
                            <p className="text-xs text-default-400">{product.category.name.toUpperCase()}</p>
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
                                <DropdownItem key="amount" onPress={() => handleOpenModal('edit')}>Change amount</DropdownItem>
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

            {/*{modalType === 'edit' && (*/}
            {/*    <EditproductForm product={product} isOpen={isOpen} onOpenChange={handleCloseModal} />*/}
            {/*)}*/}

            {/*{modalType === 'delete' && (*/}
            {/*    <DeleteproductForm product={product} isOpen={isOpen} onOpenChange={handleCloseModal} />*/}
            {/*)}*/}
        </>
    );
};

export default ProductCard;