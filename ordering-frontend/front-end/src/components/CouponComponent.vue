<template>
  <v-container>
    <v-card>
      <v-card-title>
        쿠폰 발급 대상 아이템 목록
        <v-spacer></v-spacer>
        <v-checkbox
          v-model="selectAll"
          @change="toggleAll"
          :label="`전체선택 (${selectedItems.length}/${items.length})`"
        ></v-checkbox>
        <v-btn color="primary" @click="openSelectCouponModal">발급하기</v-btn>
        <v-btn color="secondary" @click="showCouponModal = true">쿠폰 생성하기</v-btn>
      </v-card-title>
      <v-card-text>
        <v-data-table
        :headers="headers"
        :items="items"
        item-key="id"
        class="elevation-1"
        show-select
        v-model="selectedItems"
      >
        <template v-slot:[`item.totalScore`]="{ item }">
          {{ item.totalScore.toFixed(2) }}
        </template>
      </v-data-table>
      </v-card-text>
    </v-card>
    <!-- 쿠폰 생성 모달 -->
    <coupon-create-modal
      :value="showCouponModal"
      @update:value="showCouponModal = $event"
      @created="fetchAvailableCoupons"
    ></coupon-create-modal>
    <!-- 쿠폰 선택 모달 -->
    <v-dialog v-model="showSelectCouponModal" max-width="600px">
      <v-card>
        <v-card-title>쿠폰 선택</v-card-title>
        <v-card-text>
          <v-radio-group v-model="selectedCouponId">
            <v-radio
              v-for="coupon in coupons"
              :key="coupon.id"
              :label="`${coupon.name} (${coupon.startDate} - ${coupon.endDate}) 할인율: ${coupon.rateDiscount}% 정액할인: ${coupon.fixDiscount}원 선착순: ${coupon.firstCome}명`"
              :value="coupon.id"
            ></v-radio>
          </v-radio-group>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="showSelectCouponModal = false">취소</v-btn>
          <v-btn color="green darken-1" text @click="proceedWithCouponIssue">선택 완료</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios';
import CouponCreateModal from './CouponCreateModal.vue';

export default {
  components: {
    CouponCreateModal
  },
  data() {
    return {
      items: [],
      selectedItems: [],
      selectAll: false,
      showCouponModal: false,
      showSelectCouponModal: false,
      selectedCouponId: null,
      coupons: [],
      headers: [
        { text: '이름', value: 'name' },
        { text: '가격', value: 'price' },
        { text: '평점', value: 'totalScore' },
        { text: '리뷰 수', value: 'reviewNumber' },
        { text: '최소수량', value: 'minimumStock' }
      ]
    };
  },
  methods: {
    fetchItems() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      axios.get(`${process.env.VUE_APP_API_BASE_URL}/item-service/item/myItem`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "X-Refresh-Token": refreshToken
        }
      }).then(response => {
        this.items = response.data.result;
        this.items.forEach(item => {
          item.totalScore = item.reviewNumber > 0 ? item.score / item.reviewNumber : 0;
        });
      }).catch(error => {
        console.error('Error fetching items:', error);
      });
    },
    fetchAvailableCoupons() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/coupondetail`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "X-Refresh-Token": refreshToken
        }
      }).then(response => {
        this.coupons = response.data.result;
      }).catch(error => {
        console.error('Error fetching available coupons:', error);
      });
    },
    openSelectCouponModal() {
      if (this.selectedItems.length === 0) {
        alert('발급 대상자를 선택해주세요.');
        return;
      }
      this.fetchAvailableCoupons();
      this.showSelectCouponModal = true;
    },
    proceedWithCouponIssue() {
      this.issueCoupons();
      this.showSelectCouponModal = false;
    },
    issueCoupons() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/coupon/create`, {
        itemId: this.selectedItems,
        couponDetailId: this.selectedCouponId
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
          "X-Refresh-Token": refreshToken
        }
      }).then(() => {
        alert(`모든 쿠폰이 성공적으로 발급되었습니다.`);
      }).catch(error => {
        console.error('Error issuing coupons:', error);
        alert('쿠폰 발급에 실패했습니다.');
      });
    },
    toggleAll() {
      this.selectedItems = this.selectAll ? this.items.map(item => item.id) : [];
    }
  },
  created() {
    this.fetchItems();
    this.fetchAvailableCoupons();
  },
  watch: {
    selectedItems(newVal) {
      this.selectAll = newVal.length === this.items.length;
    }
  }
};
</script>