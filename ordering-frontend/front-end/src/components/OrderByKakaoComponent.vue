<template>
    <v-main>
      <v-container>
        <v-subheader>주문 완료!</v-subheader>
      </v-container>
    </v-main>
  </template>
  
<script >
  import axios from 'axios';
  import {mapGetters} from 'vuex'
//   import { mapActions } from 'vuex';
  export default {
    data(){
        return{
            buyItems: JSON.parse(localStorage.getItem('buyItem')),
            order : JSON.parse(localStorage.getItem('order')),
        }
    },
    created(){
        this.createOrder();
    },
    computed:{
        ...mapGetters(['getCartItems','getTotalQuantity'])
    },
    methods:{
        async createOrder(){
            const token = localStorage.getItem('accessToken');
            const refreshToken = localStorage.getItem('refreshToken');
            const email = localStorage.getItem('email');
            try {
                const headers = token ? { Authorization: `Bearer ${token}`, 'X-Refresh-Token': `${refreshToken}` } : {};
                const body = { totalPrice: this.order.totalPrice, addressId:"1" ,email:email ,paymentMethod:'KAKAO',recipient:this.order.name, orderDetailRequestDtoList: this.buyItems };
                const data = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/order-service/create`, body, { headers });
                console.log(data);
                let buyItemMap = {};
                this.buyItems.forEach(item => {
                    buyItemMap[item.id] = true;
                });
                let cartItem = [...this.getCartItems];
                this.$store.dispatch('clearCart');
                localStorage.removeItem('buyItem');
                localStorage.removeItem('order');
                let filteredCartItem = cartItem.filter(item => !buyItemMap[item.id]).map(item => this.$store.dispatch('addToCart',item));
                // localStorage.setItem('totalQuantity',filteredCartItem.size());
                console.log(filteredCartItem);
            }catch (e) {
                console.log(e);
            }
        }
    }
}
</script>