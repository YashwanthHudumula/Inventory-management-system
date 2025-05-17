import axios from "axios";
import CryptoJS from "crypto-js";

export default class ApiService {

    static BASE_URL = "http://localhost:5050/api";
    static ENCRYPTION_KEY = "Yash090105";


    //encrypt data using cryptoJs
    static encrypt(data) {
        return CryptoJS.AES.encrypt(data, this.ENCRYPTION_KEY.toString());
    }

    //decrypt data using cryptoJs
    static decrypt(data) {
        const bytes = CryptoJS.AES.decrypt(data, this.ENCRYPTION_KEY);
        return bytes.toString(CryptoJS.enc.Utf8);
    }

    //save token with encryption
    static saveToken(token) {
        const encryptedToken = this.encrypt(token);
        localStorage.setItem("token", encryptedToken)
    }

    // retreive the token
    static getToken() {
        const encryptedToken = localStorage.getItem("token");
        if (!encryptedToken) return null;
        return this.decrypt(encryptedToken);
    }

    //save Role with encryption
    static saveRole(role) {
        const encryptedRole = this.encrypt(role);
        localStorage.setItem("role", encryptedRole)
    }

    // retreive the role
    static getRole() {
        const encryptedRole = localStorage.getItem("role");
        if (!encryptedRole) return null;
        return this.decrypt(encryptedRole);
    }

    static clearAuth() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }


    static getHeader() {
        const token = this.getToken();
        return {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    }

    /**  AUTH && USERS API */

    static async registerUser(registerData) {
        const response = await axios.post(`${this.BASE_URL}/auth/register`, registerData)
        return response.data;
    }


    static async loginUser(loginData) {
        const response = await axios.post(`${this.BASE_URL}/auth/login`, loginData)
        return response.data;
    }


    static async getAllUsers() {
        const response = await axios.get(`${this.BASE_URL}/users/all`, {
            headers: this.getHeader()
        });
        return response.data;
    }


    static async getLoggedInUsesInfo() {
        const response = await axios.get(`${this.BASE_URL}/users/current`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async getUserById(userId) {
        const response = await axios.get(`${this.BASE_URL}/users/${userId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async updateUser(userId, userData) {
        const response = await axios.put(`${this.BASE_URL}/users/update/${userId}`, userData, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async deleteUser(userId) {
        const response = await axios.delete(`${this.BASE_URL}/users/update/${userId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }




    /**PRODUCT ENDPOINTS */

    static async addProduct(formData) {

        const response = await axios.post(`${this.BASE_URL}/products/add`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    }

    static async updateProduct(formData) {

        const response = await axios.put(`${this.BASE_URL}/products/update`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    }

    static async getAllProducts() {
        const response = await axios.get(`${this.BASE_URL}/products/all`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async getProductById(productId) {
        const response = await axios.get(`${this.BASE_URL}/products/${productId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async searchProduct(searchValue) {
        const response = await axios.get(`${this.BASE_URL}/products/search`, {
            params: { searchValue },
            headers: this.getHeader()
        });
        return response.data;
    }

    static async deleteProduct(productId) {
        const response = await axios.delete(`${this.BASE_URL}/products/delete/${productId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }



    /**CATEGOTY EDNPOINTS */
    static async createCategory(category) {
        const response = await axios.post(`${this.BASE_URL}/categories/add`, category, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async getAllCategory() {
        const response = await axios.get(`${this.BASE_URL}/categories/all`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async getCategoryById(categoryId) {
        const response = await axios.get(`${this.BASE_URL}/categories/${categoryId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async updateCategory(categoryId, categoryData) {
        const response = await axios.put(`${this.BASE_URL}/categories/update/${categoryId}`, categoryData, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async deleteCategory(categoryId) {
        const response = await axios.delete(`${this.BASE_URL}/categories/delete/${categoryId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }


    /**Supplier EDNPOINTS */
    static async addSupplier(supplierData) {
        const response = await axios.post(`${this.BASE_URL}/suppliers/add`, supplierData, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async getAllSuppliers() {
        const response = await axios.get(`${this.BASE_URL}/suppliers/all`, {
            headers: this.getHeader()
        })
        return response.data;
    }


    static async getSupplierById(supplierId) {
        const response = await axios.get(`${this.BASE_URL}/suppliers/${supplierId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async updateSupplier(supplierId, supplierData) {
        const response = await axios.put(`${this.BASE_URL}/suppliers/update/${supplierId}`, supplierData, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async deleteSupplier(supplierId) {
        const response = await axios.delete(`${this.BASE_URL}/suppliers/delete/${supplierId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }







    /**Transactions EDNPOINTS */
    static async purchaseProduct(body) {
        const response = await axios.post(`${this.BASE_URL}/transactions/purchase`, body, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async sellProduct(body) {
        const response = await axios.post(`${this.BASE_URL}/transactions/sell`, body, {
            headers: this.getHeader()
        })
        return response.data;
    }
    

    static async returnToSupplier(body) {
        const response = await axios.post(`${this.BASE_URL}/transactions/return`, body, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async getAllTransactions(filter) {
        const response = await axios.get(`${this.BASE_URL}/transactions/all`, {
            headers: this.getHeader(),
            params: {filter}
        })
        return response.data;
    }

    static async geTransactionsByMonthAndYear(month, year) {
        const response = await axios.get(`${this.BASE_URL}/transactions/by-month-year`, {
            headers: this.getHeader(),
            params: {
                month:month,
                year:year

            }
        })
        return response.data;
    }

    static async getTransactionById(transactionId) {
        const response = await axios.get(`${this.BASE_URL}/transactions/${transactionId}`, {
            headers: this.getHeader()
        })
        return response.data;
    }

    static async updateTransactionStatus(transactionId, status) {
        const response = await axios.put(`${this.BASE_URL}/transactions/${transactionId}`, status, {
            headers: this.getHeader()
        })
        return response.data;
    }


    /**AUTHENTICATION CHECKER */
    static logout(){
        this.clearAuth()
    }

    static isAuthenticated(){
        const token = this.getToken();
        return !!token;
    }

    static isAdmin(){
        const role = this.getRole();
        return role === "ADMIN";
    }

}