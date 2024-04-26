<template>
  <v-container fluid style="max-width: 55vw">
    <div style="height: 50px"></div>
    <v-card outlined tile class="pa-3 mb-3">
      <v-row>
        <v-col cols="12" md="4">
          <v-img
            v-if="member.gender === 'FEMALE'"
            src="@/assets/female.jpg"
            class="mb-3"
            style="height: 200px; width: 200px; object-fit: contain"
          ></v-img>
          <v-img
            v-else
            src="@/assets/male.jpg"
            class="mb-3"
            style="height: 200px; width: 200px; object-fit: contain"
          ></v-img>
        </v-col>
        <v-col cols="12" md="8">
          <v-card-title class="headline mb-1">
            {{ member.name }} 님</v-card-title
          >
          <v-card-subtitle class="mb-1"
            >이메일: {{ member.email }}</v-card-subtitle
          >
          <v-card-subtitle class="mb-1">나이: {{ member.age }}</v-card-subtitle>
          <v-card-subtitle class="mb-1">
            성별:
            <template v-if="member.gender === 'FEMALE'">여자</template>
            <template v-else>남자</template>
          </v-card-subtitle>
          <v-card-subtitle class="mb-1"
            >전화번호: {{ member.phoneNumber }}</v-card-subtitle
          >
          <v-col cols="12" class="text-right">
            <v-btn
              color="gray"
              class="mr-2"
              @click="showUpdateUserInfoModal = true"
              >회원정보 수정</v-btn
            >
            <v-btn color="gray" class="mr-2" @click="showCouponModal = true"
              >쿠폰확인</v-btn
            >
          </v-col>
        </v-col>
      </v-row>
    </v-card>

    <v-row>
      <v-col cols="12">
        <v-card outlined tile class="pa-3 mb-3">
          <v-card-title class="headline">나의 주문처리 현황</v-card-title>
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
      <v-col cols="6">
        <div class="text-center text-2xl font-bold">일별 구매 금액</div>
        <div class="p-4 border-2">
          <canvas
            id="dailyPurchaseAmountChart"
            width="400"
            height="200"
          ></canvas>
        </div>
      </v-col>
      <v-col cols="6">
        <div class="text-center text-2xl font-bold">일별 구매 건수</div>
        <div class="p-4 border-2">
          <canvas
            id="dailyPurchaseCountChart"
            width="400"
            height="200"
          ></canvas>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          style="height: 170px"
          @click="showBuyListModal = true"
        >
          <v-card-title class="headline">Order</v-card-title>
          <v-card-subtitle class="grey--text">주문내역 조회</v-card-subtitle>
          <v-card-subtitle class="mt-3" style="height: 20px"></v-card-subtitle>
          <v-card-subtitle class="grey--text small-text"
            >고객님께서 주문하신 상품의</v-card-subtitle
          >
          <v-card-subtitle class="grey--text small-text"
            >주문내역을 확인하실 수 있습니다.</v-card-subtitle
          >
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          style="height: 170px"
          @click="showAddressModal = true"
        >
          <v-card-title class="headline">Address</v-card-title>
          <v-card-subtitle class="grey--text">배송 주소록 관리</v-card-subtitle>
          <v-card-subtitle class="mt-3" style="height: 20px"></v-card-subtitle>
          <v-card-subtitle class="grey--text small-text"
            >자주 사용하는 배송지를 등록하고</v-card-subtitle
          >
          <v-card-subtitle class="grey--text small-text"
            >관리하실 수 있습니다.</v-card-subtitle
          >
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          style="height: 170px"
          @click="showLikeSellerListModal = true"
        >
          <v-card-title class="headline">WishList</v-card-title>
          <v-card-subtitle class="grey--text">관심 판매자</v-card-subtitle>
          <v-card-subtitle class="mt-3" style="height: 20px"></v-card-subtitle>
          <v-card-subtitle class="grey--text small-text"
            >관심 판매자로 등록하신</v-card-subtitle
          >
          <v-card-subtitle class="grey--text small-text"
            >판매자 목록을 보여드립니다.</v-card-subtitle
          >
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          style="height: 170px"
          @click="registerModal()"
        >
          <v-card-title class="headline">{{ sellerModalTitle }}</v-card-title>
          <v-card-subtitle class="grey--text">판매자 등록</v-card-subtitle>
          <v-card-subtitle class="mt-3" style="height: 20px"></v-card-subtitle>
          <v-card-subtitle class="grey--text small-text"
            >물품 판매를 위해</v-card-subtitle
          >
          <v-card-subtitle class="grey--text small-text"
            >판매자 등록 여부를 관리하실 수 있습니다.</v-card-subtitle
          >
        </v-card>
      </v-col>
    </v-row>

    <AddressModal
      v-model="showAddressModal"
      @update:dialog="updateDialog('showAddressModal', $event)"
    ></AddressModal>
    <BuyListModal
      v-model="showBuyListModal"
      @update:dialog="updateDialog('showBuyListModal', $event)"
    ></BuyListModal>
    <registerSellerModal
      v-model="showRegisterSellerModal"
      @update:dialog="updateDialog('showRegisterSellerModal', $event)"
    ></registerSellerModal>
    <LikeSellerListModal
      v-model="showLikeSellerListModal"
      @update:dialog="updateDialog('showLikeSellerListModal', $event)"
    ></LikeSellerListModal>
    <UpdateUserInfoModal
      v-model="showUpdateUserInfoModal"
      @update:dialog="updateDialog('showUpdateUserInfoModal', $event)"
    ></UpdateUserInfoModal>
    <CouponModal
      v-model="showCouponModal"
      @update:dialog="updateDialog('showCouponModal', $event)"
    ></CouponModal>
  </v-container>
</template>

<script>
import { ref } from "vue";
import axios from "axios";
import Chart from "chart.js/auto";
import AddressModal from "@/components/AddressModal.vue";
import BuyListModal from "@/components/BuyListModal.vue";
import RegisterSellerModal from "@/components/RegisterSellerModal.vue";
import LikeSellerListModal from "@/components/LikeSellerListModal.vue";
import UpdateUserInfoModal from "@/components/UpdateUserInfoModal.vue";
import CouponModal from "./CouponModal.vue";

export default {
  components: {
    AddressModal,
    BuyListModal,
    RegisterSellerModal,
    LikeSellerListModal,
    UpdateUserInfoModal,
    CouponModal,
  },
  mounted() {
    // 컴포넌트가 마운트되면 localStorage에서 role 값을 가져옴
    this.userRole = localStorage.getItem("role");
    this.fetchPurchaseInfo();
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
      userRole: null,
      // 판매자 등록 상태를 나타내는 변수
      sellerModalTitle: "SellerIn",
      member: [],
      showAddressModal: false,
      showBuyListModal: false,
      showRegisterSellerModal: false,
      showLikeSellerListModal: false,
      showUpdateUserInfoModal: false,
      showCouponModal: false,
      // 구매 그래프 시각화
      dailyPurchaseAmountChartInfo: {},
      dailyPurchaseCountChartInfo: {},
      datesForPurchaseAmount: [],
      datesForPurchaseCount: [],
      purchaseAmount: [],
      purchaseCount: [],
    };
  },
  created() {
    this.getMemberInfo();
    this.checkUserRole();
  },
  methods: {
    async getMemberInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/member-service/member`,
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
    checkUserRole() {
      const role = localStorage.getItem("role");
      if (role === "SELLER") {
        // 판매자인 경우 버튼의 제목을 '판매자 등록 취소'로 설정
        this.sellerModalTitle = "SellerOut";
      }
    },
    registerModal() {
      const role = localStorage.getItem("role");
      if (role === "SELLER") {
        // 판매자인 경우 버튼의 제목을 '판매자 등록 취소'로 설정
        alert("판매자 등록을 취소하겠습니까?");
      } else {
        this.showRegisterSellerModal = true;
      }
    },
    toggleRegisterSellerModal() {
      this.showRegisterSellerModal = !this.showRegisterSellerModal;
      if (this.showRegisterSellerModal) {
        this.sellerModalTitle = "판매자 등록 취소";
      } else {
        this.sellerModalTitle = "판매자 등록";
      }
    },
    async fetchPurchaseInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response1 = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/total_price`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.dailyPurchaseAmountChartInfo = response1.data.result;
        console.log("dailyPurchaseAmountChartInfo : ");
        console.log(this.dailyPurchaseAmountChartInfo);

        for (var i = 0; i < this.dailyPurchaseAmountChartInfo.length; i++) {
          this.datesForPurchaseAmount.push(
            this.dailyPurchaseAmountChartInfo[i].createdTime
          );
          this.purchaseAmount.push(this.dailyPurchaseAmountChartInfo[i].price);
        }

        this.dailyPurchaseAmountChart();

        const response2 = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/total_count`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.dailyPurchaseCountChartInfo = response2.data.result;
        console.log("dailyPurchaseCountChartInfo : ");
        console.log(this.dailyPurchaseCountChartInfo);

        for (var j = 0; j < this.dailyPurchaseCountChartInfo.length; j++) {
          this.datesForPurchaseCount.push(
            this.dailyPurchaseCountChartInfo[j].createdTime
          );
          this.purchaseCount.push(this.dailyPurchaseCountChartInfo[j].count);
        }

        this.dailyPurchaseCountChart();
      } catch (error) {
        console.log(error);
      }
    },
    dailyPurchaseAmountChart() {
      const ctx = document
        .getElementById("dailyPurchaseAmountChart")
        .getContext("2d");
      new Chart(ctx, {
        type: "bar",
        data: {
          labels: this.datesForPurchaseAmount,
          datasets: [
            {
              data: this.purchaseAmount,
              backgroundColor: [
                "rgba(255, 99, 132, 0.2)",
                "rgba(245, 124, 0, 0.2)",
                "rgba(255, 206, 86, 0.2)",
                "rgba(75, 192, 192, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(0, 0, 128, 0.2)",
                "rgba(153, 102, 255, 0.2)",
              ],
              borderColor: [
                "rgba(255, 99, 132, 1)",
                "rgba(245, 124, 0, 1)",
                "rgba(255, 206, 86, 1)",
                "rgba(75, 192, 192, 1)",
                "rgba(54, 162, 235, 1)",
                "rgba(0, 0, 128, 1)",
                "rgba(153, 102, 255, 1)",
              ],
              borderWidth: 1,
            },
          ],
        },
        options: {
          maintainAspectRatio: false,
          aspectRatio: 1,
          scales: {
            x: {
              grid: {
                display: false,
              },
            },
            y: {
              grid: {
                display: false,
              },
            },
          },
          plugins: {
            legend: {
              display: false,
            },
          },
        },
      });
    },
    dailyPurchaseCountChart() {
      const ctx = document
        .getElementById("dailyPurchaseCountChart")
        .getContext("2d");
      new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: this.datesForPurchaseCount,
          datasets: [
            {
              data: this.purchaseCount,
              backgroundColor: [
                "rgba(255, 99, 132, 0.2)",
                "rgba(245, 124, 0, 0.2)",
                "rgba(255, 206, 86, 0.2)",
                "rgba(75, 192, 192, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(0, 0, 128, 0.2)",
                "rgba(153, 102, 255, 0.2)",
              ],
              borderColor: [
                "rgba(255, 99, 132, 1)",
                "rgba(245, 124, 0, 1)",
                "rgba(255, 206, 86, 1)",
                "rgba(75, 192, 192, 1)",
                "rgba(54, 162, 235, 1)",
                "rgba(0, 0, 128, 1)",
                "rgba(153, 102, 255, 1)",
              ],
              borderWidth: 1,
            },
          ],
        },
        options: {
          maintainAspectRatio: false,
          aspectRatio: 1,
          scales: {
            x: {
              grid: {
                display: false,
              },
            },
            y: {
              grid: {
                display: false,
              },
            },
          },
          plugins: {
            legend: {
              display: false,
            },
          },
        },
      });
    },
    updateDialog(modalName, newVal) {
      this[modalName] = newVal;
    },
    editUserInfo() {},
    viewCoupons() {
      // 로직 구현
    },
    // 필요한 나머지 메소드 구현
  },
};
</script>
<style scoped>
.small-text {
  font-size: 11px;
}
</style>
