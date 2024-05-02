<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1500px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        판매내역 관리
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row justify="center">
            <v-col cols="12">
              <v-table>
                <thead>
                  <tr>
                    <th class="text-center">주문번호</th>
                    <th class="text-center">주문일시</th>
                    <th class="text-center">구매자이메일</th>
                    <th class="text-center">상품정보</th>
                    <th class="text-center">주문금액</th>
                    <th class="text-center">수령인이름</th>
                    <th class="text-center">수령인번호</th>
                    <th class="text-center">결제 방식</th>
                    <th class="text-center">주문상태</th>
                    <th class="text-center"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="sales in salesList" :key="sales.id">
                    <td class="text-center">{{ sales.orderNumber }}</td>
                    <td class="text-center">
                      {{ formatDateTime(sales.createdTime) }}
                    </td>
                    <td class="text-center">{{ sales.buyerEmail }}</td>
                    <td class="text-center">
                      {{ sales.itemName }}
                      <br />
                      {{ sales.options ? sales.options : "옵션 없음" }} /
                      {{ sales.quantity }}
                    </td>
                    <td class="text-center">{{ sales.totalPrice }}</td>
                    <td class="text-center">{{ sales.recipientName }}</td>
                    <td class="text-center">{{ sales.phoneNumber }}</td>
                    <td class="text-center">{{ sales.paymentMethod }}</td>
                    <td class="text-center">
                      <span v-if="sales.statue === 'PAIED'">결제 완료</span>
                      <span v-else-if="sales.statue === 'PREPARE_DELIVERY'"
                        >배송 준비</span
                      >
                      <span v-else-if="sales.statue === 'PROCEEDING_DELIVERY'"
                        >배송중</span
                      >
                      <span v-else-if="sales.statue === 'COMPLETE_DELIVERY'"
                        >배송 완료</span
                      >
                      <span v-else>알 수 없음</span>
                    </td>
                    <td class="text-center">
                      <v-menu :location="location">
                        <template v-slot:activator="{ props }">
                          <v-btn color="primary" dark flat v-bind="props">
                            변경
                          </v-btn>
                        </template>
                        <v-list>
                          <v-list-item
                            v-for="(status, index) in statusOptions"
                            :key="index"
                            @click="updateStatus(sales.id, status)"
                          >
                            <v-list-item-title>{{ status }}</v-list-item-title>
                          </v-list-item>
                        </v-list>
                      </v-menu>
                    </td>
                  </tr>
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
    this.getSalesList();
  },
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      salesList: [],
      statusOptions: ["결제 완료", "배송 준비", "배송중", "배송 완료"],
      location: "bottom",
    };
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
    async getSalesList() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      const role = localStorage.getItem("role");
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/mySales`,
          {
            headers: {
              myEmail: `${email}`,
              myRole: `${role}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.salesList = response.data.result;
        console.log("salesList : ");
        console.log(this.salesList);
      } catch (error) {
        console.error("판매내역 조회에 실패했습니다: ", error);
      }
    },
    formatDateTime(dateTime) {
      return dateTime.replace("T", " ");
    },
    async updateStatus(orderId, newStatus) {
      // newStatus 값에 따라 "PAID","PREPARE_DELIVERY","PROCEEDING_DELIVERY","COMPLETE_DELIVERY"을 statue에 넣어주기
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      const role = localStorage.getItem("role");
      let statue = "";
      switch (newStatus) {
        case "결제 완료":
          statue = "PAIED";
          break;
        case "배송 준비":
          statue = "PREPARE_DELIVERY";
          break;
        case "배송중":
          statue = "PROCEEDING_DELIVERY";
          break;
        case "배송 완료":
          statue = "COMPLETE_DELIVERY";
          break;
        default:
          console.error("잘못된 주문 상태입니다.");
          return;
      }
      try {
        const updateData = {
          id: orderId,
          statue: statue,
        };
        await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/order-service/updateStatus`,
          updateData,
          {
            headers: {
              myEmail: `${email}`,
              myRole: `${role}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
        this.getSalesList(); // 변경된 상태를 반영하기 위해 판매내역 다시 조회
      } catch (error) {
        console.error("주문 상태 변경에 실패했습니다: ", error);
      }
    },
  },
};
</script>
