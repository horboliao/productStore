import React from 'react';
import {Category, Product} from "@/lib/types";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader} from "@nextui-org/react";
import {useRouter} from "next/navigation";
import {deleteCategory} from "@/actions/categories";
import {deleteProduct} from "@/actions/products";

interface DeleteProductForm {
    product: Product;
    isOpen: boolean;
    onOpenChange: () => void;
}
const DeleteProductForm = ({product, isOpen, onOpenChange}:DeleteProductForm) => {
    const router = useRouter();

    async function onDelete() {
        await deleteProduct(product.id)
        router.refresh();
    }

    return (
        <Modal
            isOpen={isOpen}
            onOpenChange={onOpenChange}
            placement="top-center"
        >
            <ModalContent>
                {(onClose) => (
                    <>
                        <ModalHeader className="flex flex-col gap-1">Delete Product</ModalHeader>
                        <ModalBody>
                            <div>
                                <h3 className='font-semibold'>{product.name}</h3>
                            </div>
                            <p className="text-small text-default-400">{product.description}</p>
                        </ModalBody>
                        <ModalFooter>
                            <Button color="danger" onPress={onDelete}>
                                Delete
                            </Button>
                        </ModalFooter>
                    </>
                )}
            </ModalContent>
        </Modal>
    );
};

export default DeleteProductForm;