<template>
  <v-container fluid>
    <!-- 회원정보 섹션 -->
    <v-row>
      <v-col cols="12">
        <v-card outlined tile class="pa-3 mb-3">
          <v-row align="center" justify="space-between">
            <v-col cols="12" md="8">
              <v-card-title class="headline mb-1">회원정보</v-card-title>
              <v-card-subtitle class="mb-1"
                >이름: {{ member.name }}</v-card-subtitle
              >
              <v-card-subtitle class="mb-1"
                >아이디: {{ member.email }}</v-card-subtitle
              >
              <v-card-subtitle class="mb-1"
                >나이: {{ member.age }}</v-card-subtitle
              >
              <v-card-subtitle class="mb-1"
                >성별: {{ member.gender }}</v-card-subtitle
              >
              <v-card-subtitle class="mb-1"
                >전화번호: {{ member.phoneNumber }}</v-card-subtitle
              >
            </v-col>
            <v-col cols="12" md="4" class="text-right">
              <v-btn color="primary" class="mr-2" @click="editUserInfo"
                >수정</v-btn
              >
              <v-btn color="primary" class="mr-2" @click="viewCoupons"
                >쿠폰 확인</v-btn
              >
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-card outlined tile class="pa-3 mb-3">
          <v-card-title class="headline">주문관리</v-card-title>
          <v-row>
            <v-col
              cols="3"
              v-for="(orderStatus, index) in orderInfo"
              :key="index"
            >
              <v-card outlined tile class="pa-3">
                <v-card-subtitle>{{ orderStatus.name }}</v-card-subtitle>
                <v-card-text>{{ orderStatus.quantity }}</v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          @click="showBuyListModal = true"
        >
          <v-card-title class="headline">구매내역</v-card-title>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          @click="showSellListModal = true"
        >
          <v-card-title class="headline">판매내역</v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card outlined tile class="pa-3 mb-3">
          <v-card-title class="headline">즐겨찾기한 판매자 목록</v-card-title>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          @click="showAddressModal = true"
        >
          <v-card-title class="headline">배송지 관리</v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <AddressModal
      v-model="showAddressModal"
      @update:dialog="updateDialog"
    ></AddressModal>
    <BuyListModal
      v-model="showBuyListModal"
      @update:dialog="updateDialog"
    ></BuyListModal>
    <SellListModal
      v-model="showSellListModal"
      @update:dialog="updateDialog"
    ></SellListModal>
  </v-container>
</template>

<script>
import { ref } from "vue";
import axios from "axios";
import AddressModal from "@/components/AddressModal.vue";
import BuyListModal from "@/components/BuyListModal.vue";
import SellListModal from "@/components/SellListModal.vue";

export default {
  components: {
    AddressModal,
    BuyListModal,
    SellListModal,
  },
  setup() {
    const orderInfo = ref([
      { name: "입금전", quantity: 0 },
      { name: "배송준비중", quantity: 0 },
      { name: "배송중", quantity: 0 },
      { name: "배송완료", quantity: 0 },
    ]);
    return {
      orderInfo,
    };
  },
  data() {
    return {
      member: [],
      showAddressModal: false,
      showBuyListModal: false,
      showSellListModal: false,
    };
  },
  created() {
    this.getMemberInfo();
  },
  methods: {
    async getMemberInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/member-service/member/${email}`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.member = response.data.result;
        console.log(this.member);
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
    },
    updateDialog(newVal) {
      this.showAddressModal = newVal;
      this.showBuyListModal = newVal;
      this.showSellListModal = newVal;
    },
    editUserInfo() {
      // 로직 구현
    },
    viewCoupons() {
      // 로직 구현
    },
    // 필요한 나머지 메소드 구현
  },
};
</script>
