<template>
    <v-main>
      
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
            pgToken : null,
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
            // 결제 승인 코드를 넣어줌
            this.pgToken = this.$route.params.pgToken;
            if(this.pgToken === "payCanceled"){
                alert("결제를 취소 했습니다.");
                this.$router.push('/');
            }
            if(this.pgToken === "payFailed"){
                alert("결제에 실패 했습니다.");
                this.$router.push('/');
            }
            try {
              console.log(this.pgToken);
                const headers = token ? { Authorization: `Bearer ${token}`, 'X-Refresh-Token': `${refreshToken}` } : {};
                const body = { pgToken : this.pgToken, totalPrice: this.order.totalPrice, addressId:this.order.addressId ,email:email ,paymentMethod:'KAKAO',recipient:this.order.name, orderDetailRequestDtoList: this.buyItems };
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
                alert("아이템 주문에 성공했습니다.");
                this.$router.push('/');
            }catch (e) {
                console.log(e);
            }
        }
    }
}
</script>