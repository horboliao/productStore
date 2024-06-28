import { auth } from "@/auth"

export default auth((req) => {
    const pathname = req.nextUrl.pathname;
    if (!req.auth && pathname !== "/login") {
        const newUrl = new URL("/login", req.nextUrl.origin);
        return Response.redirect(newUrl);
    }
})

export const config = {
    matcher: ['/((?!.+\\.[\\w]+$|_next).*)', '/', '/(api|trpc)(.*)'],
}