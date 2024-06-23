import React from 'react';
import {Category, ProductWithCategory} from "@/lib/types";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader} from "@nextui-org/react";

interface DeleteProductForm {
    product: ProductWithCategory;
    isOpen: boolean;
    onOpenChange: () => void;
}
const DeleteProductForm = ({product, isOpen, onOpenChange}:DeleteProductForm) => {
    function onDelete() {
        console.log(product.id)
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
                                <p className="text-xs text-default-400">{product.category.name.toUpperCase()}</p>
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