<template>
  <v-container fluid style="max-width: 55vw">
    <div style="height: 20px"></div>
    <v-card outlined tile>
      <v-row>
        <v-col cols="9">
          <v-img
            @click="showGradeInfoDeailModal = true"
            src="@/assets/gradeInfo.png"
            class="mb-3"
            style="
              height: 50px;
              width: 100px;
              object-fit: contain;
              margin-left: 680px;
            "
          ></v-img>
        </v-col>
        <v-col cols="12" md="4">
          <v-img
            v-if="seller.businessType === '가구'"
            src="@/assets/living.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '도서'"
            src="@/assets/book.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '가전'"
            src="@/assets/appliances.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '생활'"
            src="@/assets/living.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '건강'"
            src="@/assets/health.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '스포츠'"
            src="@/assets/sports.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '식품'"
            src="@/assets/food.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '육아'"
            src="@/assets/baby.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '의류'"
            src="@/assets/clothing.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '잡화'"
            src="@/assets/item.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
          <v-img
            v-if="seller.businessType === '화장품'"
            src="@/assets/makeup.png"
            class="mb-3"
            style="
              height: 200px;
              width: 200px;
              object-fit: contain;
              margin-left: 130px;
            "
          ></v-img>
        </v-col>
        <v-col cols="12" md="8">
          <v-col cols="12" md="8">
            <v-col
              cols="auto"
              class="d-flex align-items-center justify-content-between"
            >
              <v-card-title
                class="headline mb-1"
                style="margin-left: 80px; font-size: 30px"
                >{{ seller.companyName }}</v-card-title
              >
              <v-img
                v-if="seller.totalScore >= 0 && seller.totalScore < 10"
                src="@/assets/unRank.png"
                @click="showGradeInfoDeailModal = true"
                class="mb-3 ml-2"
                style="
                  height: 60px;
                  width: 60px;
                  object-fit: contain;
                  margin-right: 30px;
                  margin-top: -10px;
                "
              ></v-img>
              <v-img
                v-if="seller.totalScore >= 10 && seller.totalScore < 20"
                src="@/assets/bronze.png"
                @click="showGradeInfoDeailModal = true"
                class="mb-3 ml-2"
                style="
                  height: 60px;
                  width: 60px;
                  object-fit: contain;
                  margin-right: 30px;
                  margin-top: -10px;
                "
              ></v-img>
              <v-img
                v-if="seller.totalScore >= 20 && seller.totalScore < 30"
                src="@/assets/iron.png"
                @click="showGradeInfoDeailModal = true"
                class="mb-3 ml-2"
                style="
                  height: 60px;
                  width: 60px;
                  object-fit: contain;
                  margin-right: 30px;
                  margin-top: -10px;
                "
              ></v-img>
              <v-img
                v-if="seller.totalScore >= 30 && seller.totalScore < 40"
                src="@/assets/silver.png"
                @click="showGradeInfoDeailModal = true"
                class="mb-3 ml-2"
                style="
                  height: 60px;
                  width: 60px;
                  object-fit: contain;
                  margin-right: 30px;
                  margin-top: -10px;
                "
              ></v-img>
              <v-img
                v-if="seller.totalScore >= 40 && seller.totalScore < 50"
                src="@/assets/gold.png"
                @click="showGradeInfoDeailModal = true"
                class="mb-3 ml-2"
                style="
                  height: 60px;
                  width: 60px;
                  object-fit: contain;
                  margin-right: 30px;
                  margin-top: -10px;
                "
              ></v-img>
              <v-img
                v-if="seller.totalScore >= 50 && seller.totalScore < 60"
                src="@/assets/platinum.png"
                @click="showGradeInfoDeailModal = true"
                class="mb-3 ml-2"
                style="
                  height: 60px;
                  width: 60px;
                  object-fit: contain;
                  margin-right: 30px;
                  margin-top: -10px;
                "
              ></v-img>
            </v-col>
            <v-card-title class="headline mb-1" style="margin-left: 80px">
              <span v-if="seller.totalScore >= 0 && seller.totalScore < 10">
                판매자님은 UNRANK입니다
              </span>
              <span
                v-else-if="seller.totalScore >= 10 && seller.totalScore < 20"
              >
                판매자님은 BRONZE입니다
              </span>
              <span
                v-else-if="seller.totalScore >= 20 && seller.totalScore < 30"
              >
                판매자님은 IRON입니다
              </span>
              <span
                v-else-if="seller.totalScore >= 30 && seller.totalScore < 40"
              >
                판매자님은 SILVER입니다
              </span>
              <span
                v-else-if="seller.totalScore >= 40 && seller.totalScore < 50"
              >
                판매자님은 GOLD입니다
              </span>
              <span
                v-else-if="seller.totalScore >= 50 && seller.totalScore < 60"
              >
                판매자님은 PLATINUM입니다
              </span>
            </v-card-title>
            <v-card-subtitle class="mb-1" style="margin-left: 80px"
              >사업 유형: {{ seller.businessType }}</v-card-subtitle
            >
            <v-card-subtitle class="mb-1" style="margin-left: 80px"
              >사업자 번호: {{ seller.businessNumber }}</v-card-subtitle
            >
          </v-col>
          <v-col cols="12" md="12" class="text-right">
            <v-btn color="gray" class="mr-2" @click="goToCouponPage"
              >쿠폰 등록</v-btn
            >
          </v-col>
        </v-col>
      </v-row>
    </v-card>
    <v-row class="mt-12">
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
    <v-row class="mt-12">
      <v-col cols="6">
        <div class="text-center text-2xl font-bold">아이템별 판매 금액</div>
        <div class="p-4 border-2">
          <canvas
            id="dailySalesAmountEachItemChart"
            width="400"
            height="200"
          ></canvas>
        </div>
      </v-col>
      <v-col cols="6">
        <div class="text-center text-2xl font-bold">아이템별 재고 변화</div>
        <div class="p-4 border-2">
          <canvas id="" width="400" height="200"></canvas>
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
          @click="showSellerItemManageModal = true"
        >
          <v-card-title class="headline">Item</v-card-title>
          <v-card-subtitle class="grey--text">물품관리</v-card-subtitle>
          <v-card-subtitle class="mt-3" style="height: 20px"></v-card-subtitle>
          <v-card-subtitle class="grey--text small-text"
            >판매자님께서 판매하실 상품의</v-card-subtitle
          >
          <v-card-subtitle class="grey--text small-text"
            >정보 및 재고를 관리하실 수 있습니다.</v-card-subtitle
          >
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card
          outlined
          tile
          class="pa-3 mb-3"
          style="height: 170px"
          @click="showSellListModal = true"
        >
          <v-card-title class="headline">History</v-card-title>
          <v-card-subtitle class="grey--text">내역관리</v-card-subtitle>
          <v-card-subtitle class="mt-3" style="height: 20px"></v-card-subtitle>
          <v-card-subtitle class="grey--text small-text"
            >판매자님의 주문에 대한</v-card-subtitle
          >
          <v-card-subtitle class="grey--text small-text"
            >정보를 확인 및 관리하실 수 있습니다.</v-card-subtitle
          >
        </v-card>
      </v-col>
    </v-row>
    <ManageItemsModal
      v-model="showManageItemsModal"
      @update:dialog="updateDialog('showManageItemsModal', $event)"
    ></ManageItemsModal>
    <SellerItemManageModal
      v-model="showBuyListModal"
      @update:dialog="updateDialog('showBuyListModal', $event)"
    ></SellerItemManageModal>
    <GradeInfoDeailModal
      v-model="showGradeInfoDeailModal"
      @update:dialog="updateDialog('showGradeInfoDeailModal', $event)"
    ></GradeInfoDeailModal>
    <SellListModal
      v-model="showSellListModal"
      @update:dialog="updateDialog('showSellListModal', $event)"
    ></SellListModal>
    <SellerItemManageModal
      v-model="showSellerItemManageModal"
      @update:dialog="updateDialog('showSellerItemManageModal', $event)"
    ></SellerItemManageModal>
  </v-container>
</template>

<script>
import axios from "axios";
import Chart from "chart.js/auto";
import ManageItemsModal from "@/components/ManageItemsModal.vue";
import SellerItemManageModal from "@/components/SellerItemManageModal.vue";
import GradeInfoDeailModal from "@/components/GradeInfoDeailModal.vue";
import SellListModal from "@/components/SellListModal.vue";
export default {
  mounted() {
    this.userRole = localStorage.getItem("role");
    this.fetchSalesInfo();
  },
  components: {
    ManageItemsModal,
    SellerItemManageModal,
    GradeInfoDeailModal,
    SellListModal,
  },
  created() {
    this.getSellerInfo();
  },
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      showManageItemsModal: false,
      showBuyListModal: false,
      showGradeInfoDeailModal: false,
      showSellListModal: false,
      showSellerItemManageModal: false,
      seller: [],
      // 판매 그래프 시각화
      dailySalesAmountChartInfo: {},
      dailySalesCountChartInfo: {},
      datesForSalesAmount: [],
      datesForSalesCount: [],
      salesAmount: [],
      salesCount: [],
      // 아이템별 매출 그래프
      dailySalesAmountEachItemChartInfo: {},
      itemNamesForEachItem: [],
      salesAmountForEachItem: [],
    };
  },
  methods: {
    async getSellerInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/seller`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.seller = response.data.result;
        console.log(this.seller);
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
    },
    goToCouponPage() {
      this.$router.push({ name: "CouponComponent" });
    },
    async fetchSalesInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response1 = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-order-service/total_price/seller`,
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
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-order-service/total_count/seller`,
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

        const response3 = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-order-service/each_item_price/seller`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.dailySalesAmountEachItemChartInfo = response3.data.result;
        console.log("dailySalesAmountEachItemChartInfo : ");
        console.log(this.dailySalesAmountEachItemChartInfo);

        for (
          var k = 0;
          k < this.dailySalesAmountEachItemChartInfo.length;
          k++
        ) {
          this.itemNamesForEachItem.push(
            this.dailySalesAmountEachItemChartInfo[k].itemName
          );
          this.salesAmountForEachItem.push(
            this.dailySalesAmountEachItemChartInfo[k].price
          );
        }

        this.dailySalesAmountEachItemChart();
      } catch (error) {
        console.log(error);
      }
    },
    dailySalesAmountChart() {
      const ctx = document
        .getElementById("dailySalesAmountChart")
        .getContext("2d");
      new Chart(ctx, {
        type: "bar",
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
        type: "doughnut",
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
    dailySalesAmountEachItemChart() {
      const ctx = document
        .getElementById("dailySalesAmountEachItemChart")
        .getContext("2d");
      new Chart(ctx, {
        type: "pie",
        data: {
          labels: this.itemNamesForSalesAmount,
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
    close() {
      this.$emit("update:dialog", false);
    },
    updateDialog(modalName, newVal) {
      this[modalName] = newVal;
    },
  },
};
</script>
