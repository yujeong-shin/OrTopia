<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        구매내역
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row justify="center">
            <v-col cols="12">
              <v-table>
                <thead>
                  <tr>
                    <th class="text-center">주문 번호</th>
                    <th class="text-center">주문 시간</th>
                    <th class="text-center">주문 금액</th>
                    <th class="text-center">주문 상태</th>
                    <th class="text-center">주문자 이메일</th>
                    <th class="text-center">수령자 이름</th>
                    <th class="text-center">결제 방식</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-for="(order, index) in orderList" :key="order.id">
                    <tr @click="toggleDetail(index)">
                      <td class="text-center">{{ order.id }}</td>
                      <td class="text-center">
                        {{ formatDateTime(order.createdTime) }}
                      </td>
                      <td class="text-center">{{ order.totalPrice }}</td>
                      <td class="text-center">{{ order.statue }}</td>
                      <td class="text-center">{{ order.email }}</td>
                      <td class="text-center">{{ order.recipient }}</td>
                      <td class="text-center">{{ order.paymentMethod }}</td>
                    </tr>
                    <template v-if="order.showDetails">
                      <tr>
                        <td colspan="10">
                          <v-card outlined class="ma-7">
                            <v-card-title class="headline text-center">
                              상세 주문 정보
                            </v-card-title>
                            <v-card-text>
                              <v-row no-gutters>
                                <v-col cols="12" sm="2" class="text-center ma-2"
                                  >상품 ID</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center ma-2"
                                  >사진</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center ma-2"
                                  >수량</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center ma-2"
                                  >상품명</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center ma-2"
                                  >가격</v-col
                                >
                              </v-row>
                              <v-row
                                v-for="detail in orderDetailList"
                                :key="detail.id"
                              >
                                <v-col
                                  cols="12"
                                  sm="2"
                                  class="text-center ma-2"
                                  >{{ detail.itemId }}</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center ma-2"
                                  ><v-img
                                    :src="detail.itemInfo?.imagePath"
                                    height="100px"
                                  ></v-img
                                ></v-col>
                                <v-col
                                  cols="12"
                                  sm="2"
                                  class="text-center ma-2"
                                  >{{ detail.quantity }}</v-col
                                >
                                <v-col
                                  cols="12"
                                  sm="2"
                                  class="text-center ma-2"
                                  >{{ detail.itemInfo?.name }}</v-col
                                >
                                <v-col
                                  cols="12"
                                  sm="2"
                                  class="text-center ma-2"
                                  >{{ detail.itemInfo?.price }}</v-col
                                >
                              </v-row>
                            </v-card-text>
                          </v-card>
                        </td>
                      </tr>
                    </template>
                  </template>
                </tbody>
              </v-table>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";

export default {
  created() {
    this.getOrderList();
  },
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      orderList: [],
      orderDetailList: [],
    };
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
    async getOrderList() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/all_my_order_detail`,
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.orderList = response.data.result;
      } catch (error) {
        console.error("주문 상세조회에 실패했습니다: ", error);
      }
    },
    formatDateTime(dateTime) {
      return dateTime.replace("T", " ");
    },
    async toggleDetail(index) {
      this.orderList[index].showDetails = !this.orderList[index].showDetails;
      this.orderDetailList = this.orderList[index].orderDetailResponseDtoList;

      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");

      for (var i = 0; i < this.orderDetailList.length; i++) {
        try {
          const response = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/item-service/item/read/${this.orderDetailList[i].itemId}/my_page`,
            {
              headers: {
                myEmail: `${email}`,
                Authorization: `Bearer ${token}`,
                "X-Refresh-Token": `${refreshToken}`,
              },
            }
          );
          this.orderDetailList[i].itemInfo = response.data.result;
          console.log("this.orderDetailList[i].itemInfo : ");
          console.log(this.orderDetailList[i].itemInfo);
        } catch (error) {
          console.error("주문 상세조회에 실패했습니다: ", error);
        }
      }
    },
  },
};
</script>

<style>
.col-width {
  width: 25%;
}
</style>
