import React from 'react';
import {Category} from "@/lib/types";
import {Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button} from "@nextui-org/react";
import {deleteCategory, updateCategory} from "@/actions/categories";
import {useRouter} from "next/navigation";
interface CategoryForm {
    category: Category;
    isOpen: boolean;
    onOpenChange: () => void;
}
const DeleteCategoryForm = ({category, isOpen, onOpenChange}:CategoryForm) => {
    const router = useRouter();

    async function onDelete() {
        await deleteCategory(category.id)
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
                        <ModalHeader className="flex flex-col gap-1">Delete Category</ModalHeader>
                        <ModalBody>
                            <h3 className='font-semibold'>{category.name}</h3>
                            <p className="text-small text-default-400">{category.description}</p>
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

export default DeleteCategoryForm;