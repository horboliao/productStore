import React from 'react';
import {Navbar, NavbarBrand, NavbarContent, NavbarItem, Link, Button} from "@nextui-org/react";

const NavigationPanel = () => {
    return (
        <Navbar>
            <NavbarBrand>
                <p className="font-bold text-inherit">Product Store</p>
            </NavbarBrand>
            <NavbarContent className="hidden sm:flex gap-4" justify="center">
                <NavbarItem>
                    <Link color="foreground" href="/categories">
                        Categories
                    </Link>
                </NavbarItem>
                <NavbarItem>
                    <Link color="foreground" href="/">
                        Products
                    </Link>
                </NavbarItem>
            </NavbarContent>
            <NavbarContent justify="end">
                <NavbarItem>
                    <Button as={Link} color="danger" variant="flat">
                       Log out
                    </Button>
                </NavbarItem>
            </NavbarContent>
        </Navbar>
    );
};

export default NavigationPanel;