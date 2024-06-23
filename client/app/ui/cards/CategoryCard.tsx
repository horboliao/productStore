'use client'
import React from 'react';
import {Category} from "@/lib/types";
import {Card, CardHeader, CardBody, CardFooter, Avatar, useDisclosure} from "@nextui-org/react";
import {Dropdown, DropdownTrigger, DropdownMenu, DropdownItem, Button} from "@nextui-org/react";
import {EllipsisVertical} from "lucide-react";
import EditCategoryForm from "@/app/ui/forms/category/edit-category-form";
import DeleteCategoryForm from "@/app/ui/forms/category/delete-category-form";

interface CategoryCardProps {
    category: Category
}
const CategoryCard = ({category}:CategoryCardProps) => {
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
            <Card>
                <CardHeader className="justify-between">
                    <h3 className='font-semibold'>{category.name}</h3>
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
                            <DropdownItem key="edit" onPress={() => handleOpenModal('edit')}>Edit category</DropdownItem>
                            <DropdownItem key="delete" className="text-danger" color="danger" onPress={() => handleOpenModal('delete')}>
                                Delete category
                            </DropdownItem>
                        </DropdownMenu>
                    </Dropdown>
                </CardHeader>
                <CardBody className="px-3 py-0 text-small text-default-400">
                    <p>{category.description}</p>
                </CardBody>
                <CardFooter></CardFooter>
            </Card>

            {modalType === 'edit' && (
                <EditCategoryForm category={category} isOpen={isOpen} onOpenChange={handleCloseModal} />
            )}

            {modalType === 'delete' && (
                <DeleteCategoryForm category={category} isOpen={isOpen} onOpenChange={handleCloseModal} />
            )}
        </>
    );
};

export default CategoryCard;