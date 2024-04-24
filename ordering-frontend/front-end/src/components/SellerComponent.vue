<template>
  <v-container fluid style="max-width: 55vw">
    <div style="height: 50px"></div>
    <v-row>
      <v-col cols="12" md="4"> </v-col>
      <v-col cols="12" md="8">
        <v-col cols="12" class="text-right">
          <v-btn color="gray" class="mr-2" @click="showManageItemsModal = true">판매물품 등록</v-btn>
          <v-btn color="gray" class="mr-2" @click="goToCouponPage">쿠폰 등록</v-btn>
        </v-col>
      </v-col>
      <v-row>
        <v-col cols="6">
          <div class="text-center text-2xl font-bold">일별 판매 금액</div>
          <div class="p-4 border-2">
            <canvas id="dailySalesAmountChart" width="400" height="200"></canvas>
          </div>
        </v-col>
        <v-col cols="6">
          <div class="text-center text-2xl font-bold">일별 판매 건수</div>
          <div class="p-4 border-2">
            <canvas id="dailySalesCountChart" width="400" height="200"></canvas>
          </div>
        </v-col>
      </v-row>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          @click="showBuyListModal = true"
        >
          <v-card-title class="headline">판매물품 관리</v-card-title>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          @click="showAddressModal = true"
        >
          <v-card-title class="headline">판매내역 관리</v-card-title>
        </v-card>
      </v-col>
    </v-row>
    <ManageItemsModal
      v-model="showManageItemsModal"
      @update:dialog="updateDialog('showManageItemsModal', $event)"
    ></ManageItemsModal>
  </v-container>
</template>

<script>
import axios from "axios";
import Chart from "chart.js/auto";
import ManageItemsModal from "@/components/ManageItemsModal.vue";
export default {
  mounted() {
    this.userRole = localStorage.getItem("role");
    this.fetchSalesInfo();
  },
  components: {
    ManageItemsModal,
  },
  created() {},
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      showManageItemsModal: false,

      // 판매 그래프 시각화
      dailySalesAmountChartInfo: {},
      dailySalesCountChartInfo: {},
      datesForSalesAmount: [],
      datesForSalesCount: [],
      salesAmount: [],
      salesCount: [],
    };
  },
  methods: {
    goToCouponPage() {
      this.$router.push({ name: 'CouponComponent' });
    },
    async fetchSalesInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response1 = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/total_price/seller`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.dailySalesAmountChartInfo = response1.data.result;
        console.log("dailySalesAmountChartInfo : ");
        console.log(this.dailySalesAmountChartInfo);

        for (var i = 0; i < this.dailySalesAmountChartInfo.length; i++) {
          this.datesForSalesAmount.push(
            this.dailySalesAmountChartInfo[i].createdTime
          );
          this.salesAmount.push(this.dailySalesAmountChartInfo[i].price);
        }

        this.dailySalesAmountChart();

        const response2 = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/total_count/seller`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.dailySalesCountChartInfo = response2.data.result;
        console.log("dailySalesCountChartInfo : ");
        console.log(this.dailySalesCountChartInfo);

        for (var j = 0; j < this.dailySalesCountChartInfo.length; j++) {
          this.datesForSalesCount.push(
            this.dailySalesCountChartInfo[j].createdTime
          );
          this.salesCount.push(this.dailySalesCountChartInfo[j].count);
        }

        this.dailySalesCountChart();
      } catch (error) {
        console.log(error);
      }
    },
    dailySalesAmountChart() {
      const ctx = document
        .getElementById("dailySalesAmountChart")
        .getContext("2d");
      new Chart(ctx, {
        type: "line",
        data: {
          labels: this.datesForSalesAmount,
          datasets: [
            {
              data: this.salesAmount,
              backgroundColor: [
                "rgba(54, 162, 235, 0.2)",
                "rgba(75, 192, 192, 0.2)",
                "rgba(255, 206, 86, 0.2)",
                "rgba(245, 124, 0, 0.2)",
                "rgba(255, 99, 132, 0.2)",
              ],
              borderColor: [
                "rgba(54, 162, 235, 1)",
                "rgba(75, 192, 192, 1)",
                "rgba(255, 206, 86, 1)",
                "rgba(245, 124, 0, 1)",
                "rgba(255, 99, 132, 1)",
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
    dailySalesCountChart() {
      const ctx = document
        .getElementById("dailySalesCountChart")
        .getContext("2d");
      new Chart(ctx, {
        type: "bar",
        data: {
          labels: this.datesForSalesCount,
          datasets: [
            {
              data: this.salesCount,
              backgroundColor: [
                // "rgba(0, 0, 128, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(75, 192, 192, 0.2)",
                "rgba(255, 206, 86, 0.2)",
                "rgba(245, 124, 0, 0.2)",
                "rgba(255, 99, 132, 0.2)",
              ],
              borderColor: [
                // "rgba(0, 0, 128, 1)",
                "rgba(54, 162, 235, 1)",
                "rgba(75, 192, 192, 1)",
                "rgba(255, 206, 86, 1)",
                "rgba(245, 124, 0, 1)",
                "rgba(255, 99, 132, 1)",
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
    close() {
      this.$emit("update:dialog", false);
    },
    updateDialog(modalName, newVal) {
      this[modalName] = newVal;
    },
  },
};
</script>
