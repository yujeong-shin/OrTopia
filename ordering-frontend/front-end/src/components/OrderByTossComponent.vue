<template>
  <v-main> </v-main>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      buyItems: JSON.parse(localStorage.getItem("buyItem")),
      order: JSON.parse(localStorage.getItem("order")),
      pgToken: null,
    };
  },
  created() {
    this.createOrder();
  },
  computed: {
    ...mapGetters(["getCartItems", "getTotalQuantity"]),
  },
  methods: {
    async createOrder() {
      var currentUrl = window.location.href;
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      // URL 파라미터 값을 추출하기
      var urlParams = new URLSearchParams(currentUrl);
      // urlParams.forEach(function(value, key) {
      //   console.log(key, value);
      // });
      var orderId = urlParams.get('https://www.yujeong.shop/order/toss?orderId');
      console.log(orderId);
      var paymentKey = urlParams.get('paymentKey');
      var amount = urlParams.get('amount');
      // 결제 승인 코드를 넣어줌
      if (paymentKey === null) {
        alert("결제를 취소 했습니다.");
        this.$router.push("/");
      }
      if (paymentKey === null) {
        alert("결제에 실패 했습니다.");
        this.$router.push("/");
      }
      try {
        const headers = {
          Authorization: `Bearer ${token}`,
          "X-Refresh-Token": `${refreshToken}`,
        };
        const queryParams = new URLSearchParams({
          orderId: orderId,
          paymentKey: paymentKey,
          amount: parseInt(amount, 10),
        });
        const url = `${process.env.VUE_APP_API_BASE_URL}/ortopia-order-service/toss/success?${queryParams}`;
        const data = await axios.post(url,{
          pgToken: paymentKey,
          totalPrice: this.order.totalPrice,
          addressId: this.order.addressId,
          email: localStorage.getItem("email"),
          paymentMethod: "TOSS",
          recipientName: this.order.name,
          phoneNumber: this.order.phoneNumber,
          orderDetailRequestDtoList: this.buyItems,
        }
        ,{
          headers: headers
        });
        console.log(data);
        alert("주문이 완료 됐습니다.");
        let buyItemMap = {};
        this.buyItems.forEach((item) => {
          buyItemMap[item.id] = true;
        });
        let cartItem = [...this.getCartItems];
        this.$store.dispatch("clearCart");
        localStorage.removeItem("buyItem");
        localStorage.removeItem("order");
        let filteredCartItem = cartItem
          .filter((item) => !buyItemMap[item.id])
          .map((item) => this.$store.dispatch("addToCart", item));
        // localStorage.setItem('totalQuantity',filteredCartItem.size());
        console.log(filteredCartItem);
        alert("아이템 주문에 성공했습니다.");
        this.$router.push("/");
      } catch (e) {
        let buyItemMap = {};
        this.buyItems.forEach((item) => {
          buyItemMap[item.id] = true;
        });
        let cartItem = [...this.getCartItems];
        this.$store.dispatch("clearCart");
        localStorage.removeItem("buyItem");
        localStorage.removeItem("order");
        let filteredCartItem = cartItem
          .filter((item) => !buyItemMap[item.id])
          .map((item) => this.$store.dispatch("addToCart", item));
        // localStorage.setItem('totalQuantity',filteredCartItem.size());
        console.log(filteredCartItem);
        console.log(e);
        alert("아이템 주문에 성공했습니다.");
        this.$router.push("/");
      }
    },
  },
};
</script>
