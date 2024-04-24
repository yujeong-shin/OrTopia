<template>
  <v-container>
    <v-card>
      <v-card-title>
        쿠폰 발급 대상자 목록
        <v-spacer></v-spacer>
        <v-checkbox
          v-model="selectAll"
          @change="toggleAll"
          :label="`전체선택 (${selectedBuyers.length}/${buyers.length})`"
        ></v-checkbox>
        <v-btn color="primary" @click="openSelectCouponModal">발급하기</v-btn>
        <v-btn color="secondary" @click="showCouponModal = true">쿠폰 생성하기</v-btn>
      </v-card-title>
      <v-card-text>
        <v-data-table
          :items="buyers"
          item-key="id"
          class="elevation-1"
          show-select
          v-model="selectedBuyers"
        >
          <template v-slot:[`item.age`]="{ item }">
            {{ item.age }}세
          </template>
          <template v-slot:[`item.gender`]="{ item }">
            {{ item.gender === 'FEMALE' ? '여성' : '남성' }}
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
              :label="`${coupon.name} (${coupon.startDate} - ${coupon.endDate}) 할인율: ${coupon.rateDiscount}% 정액할인: ${coupon.fixDiscount}원`"
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
      buyers: [],
      selectedBuyers: [],
      selectAll: false,
      showCouponModal: false,
      showSelectCouponModal: false,
      selectedCouponId: null,
      coupons: []
    };
  },
  methods: {
    fetchBuyers() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");

      axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/likedByBuyers`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "X-Refresh-Token": refreshToken,
          "myEmail": email
        }
      })
      .then(response => {
        this.buyers = response.data.result;
      })
      .catch(error => {
        console.error('Error fetching buyers:', error);
      });
    },
    fetchAvailableCoupons() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/coupondetail`, {
        headers: {
          Authorization: `Bearer ${token}`,
            "X-Refresh-Token": refreshToken,
            "Content-Type": "application/json",
            myEmail: email
        }
      })
      .then(response => {
        this.coupons = response.data.result;
      })
      .catch(error => {
        console.error('Error fetching available coupons:', error);
      });
    },
    openSelectCouponModal() {
      if (this.selectedBuyers.length === 0) {
        alert('발급 대상자를 선택해주세요.');
        return;
      }
      this.fetchAvailableCoupons(); // 쿠폰 목록을 가져오도록 수정
      this.showSelectCouponModal = true;
    },
    proceedWithCouponIssue() {
      this.issueCoupons();
      this.showSelectCouponModal = false;
    },
    issueCoupons() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");

      let promises = this.selectedBuyers.map(buyerId => {
        return axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/assign`, {
          memberId: buyerId,
          couponDetailId: this.selectedCouponId
        }, {
          headers: {
            Authorization: `Bearer ${token}`,
            "X-Refresh-Token": refreshToken,
            "Content-Type": "application/json",
            myEmail: email
          }
        });
      });

      Promise.all(promises)
        .then(() => {
          alert(`모든 쿠폰이 성공적으로 발급되었습니다.`);
        })
        .catch(error => {
          console.error('Error issuing coupons:', error);
          alert('쿠폰 발급에 실패했습니다.');
        });
    },
    toggleAll() {
      this.selectedBuyers = this.selectAll ? this.buyers.map(buyer => buyer.id) : [];
    }
  },
  created() {
    this.fetchBuyers();
    this.fetchAvailableCoupons();
  },
  watch: {
    selectedBuyers(newVal) {
      this.selectAll = newVal.length === this.buyers.length;
    }
  }
};
</script>