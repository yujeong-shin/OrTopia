<template>
  <v-container fluid>
    <!-- 회원정보 섹션 -->
    <v-row>
      <v-col cols="12">
        <v-card outlined tile class="pa-3 mb-3">
          <v-row align="center" justify="space-between">
            <v-col cols="12" md="8">
              <v-card-title class="headline mb-1">회원정보</v-card-title>
              <v-card-subtitle class="mb-1">이름: {{ memberInfo.name }}</v-card-subtitle>
              <v-card-subtitle class="mb-1">아이디: {{ memberId }}</v-card-subtitle>
              <v-card-subtitle>쿠폰: {{ memberInfo.coupons?.length }}개</v-card-subtitle>
            </v-col>
            <v-col cols="12" md="4" class="text-right">
              <v-btn color="primary" class="mr-2" @click="editUserInfo">수정</v-btn>
              <v-btn color="primary" class="mr-2" @click="viewCoupons">쿠폰 확인</v-btn>
              <v-btn color="primary" @click="showModal = true">배송지 목록</v-btn>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <!-- 주문관리 섹션 -->
    <v-row>
      <v-col cols="12">
        <v-card outlined tile class="pa-3 mb-3">
          <v-card-title class="headline">주문관리</v-card-title>
          <!-- 주문 상태를 나타내는 서브 섹션들 -->
          <v-row>
            <v-col cols="3" v-for="(orderStatus, index) in orderInfo" :key="index">
              <v-card outlined tile class="pa-3">
                <v-card-subtitle>{{ orderStatus.name }}</v-card-subtitle>
                <v-card-text>{{ orderStatus.quantity }}</v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <!-- 하단 섹션: 구매내역, 판매내역, 즐겨찾기한 업체 목록 -->
    <v-row>
      <v-col cols="12" md="6">
        <v-card outlined tile class="pa-3 mb-3">
          <v-card-title class="headline">구매내역</v-card-title>
          <!-- 구매내역 내용 -->
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card outlined tile class="pa-3 mb-3">
          <v-card-title class="headline">판매내역</v-card-title>
          <!-- 판매내역 내용 -->
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-card outlined tile class="pa-3">
          <v-card-title class="headline">즐겨찾기한 업체 목록</v-card-title>
          <!-- 즐겨찾기한 업체 목록 내용 -->
        </v-card>
      </v-col>
    </v-row>
    <AddressModal
      v-model="showModal"
      :member-id="memberId"
      @update:dialog="updateDialog"
    ></AddressModal>
  </v-container>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import AddressModal from "@/components/AddressModal.vue";

export default {
  components: {
    AddressModal
  },
  setup() {
    const orderInfo = ref([
      { name: '입금전', quantity: 0 },
      { name: '배송준비중', quantity: 0 },
      { name: '배송중', quantity: 0 },
      { name: '배송완료', quantity: 0 }
    ]);
    // 나머지 로직 구현
    return {
      orderInfo
      // 다른 반응형 데이터를 여기에 반환
    };
  },
  data() {
    return {
      memberInfo: {},
      showModal: false,
    };
  },
  created() {
    this.fetchMemberInfo();
  },
  methods: {
    async fetchMemberInfo() {
      if (!this.memberId) {
        console.error("Member ID is not provided.");
        return;
      }
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/${this.memberId}`);
        this.memberInfo = response.data;
      } catch (error) {
        console.error("Error fetching member information:", error);
      }
    },
    updateDialog(newVal) {
      this.showModal = newVal;
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