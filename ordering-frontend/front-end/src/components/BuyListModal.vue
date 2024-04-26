<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1500px">
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
                    <th class="text-center">결제 금액</th>
                    <th class="text-center">주문자 이메일</th>
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
                      <td class="text-center">{{ order.email }}</td>
                      <td class="text-center">{{ order.paymentMethod }}</td>
                    </tr>
                    <template v-if="order.showDetails">
                      <tr>
                        <td colspan="10">
                          <v-card outlined>
                            <v-card-title class="headline text-center">
                              상세 주문 정보
                            </v-card-title>
                            <v-card-text>
                              <v-row no-gutters>
                                <v-col cols="12" sm="2" class="text-center"
                                  >사진</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center"
                                  >수량</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center"
                                  >옵션</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center"
                                  >상품명</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center"
                                  >가격</v-col
                                >
                                <v-col cols="12" sm="2" class="text-center"
                                  >주문 상태</v-col
                                >
                                <v-col
                                  cols="12"
                                  sm="2"
                                  class="text-center"
                                ></v-col>
                                <!-- 리뷰 쓰기 버튼 추가 -->
                              </v-row>
                              <v-row
                                v-for="detail in orderDetailList"
                                :key="detail.id"
                              >
                                <v-col cols="12" sm="2" class="text-center">
                                  <v-img
                                    :src="detail.itemInfo?.imagePath"
                                    height="100px"
                                  ></v-img>
                                </v-col>
                                <v-col cols="12" sm="2" class="text-center">{{
                                  detail.quantity
                                }}</v-col>
                                <v-col cols="12" sm="2" class="text-center">{{
                                  detail.options
                                }}</v-col>
                                <v-col cols="12" sm="2" class="text-center">{{
                                  detail.itemInfo?.name
                                }}</v-col>
                                <v-col cols="12" sm="2" class="text-center">{{
                                  detail.itemInfo?.price
                                }}</v-col>
                                <v-col cols="12" sm="2" class="text-center">{{
                                  detail.statue
                                }}</v-col>
                                <v-col cols="12" sm="2" class="text-center">
                                  <v-btn
                                    v-if="!detail.reviewed"
                                    color="primary"
                                    class="ma-2"
                                    @click="openNestedModal(detail)"
                                    >리뷰 작성</v-btn
                                  >
                                  <v-btn
                                    v-if="detail.reviewed"
                                    color="primary"
                                    class="ma-2"
                                    @click="openNestedModal()"
                                    >리뷰 수정하기</v-btn
                                  >
                                  <ReviewModal
                                    v-model="nestedModalOpen"
                                    :dialog="nestedModalOpen"
                                    :detail="selectedDetail"
                                    @update:dialog="nestedModalOpen = $event"
                                  />
                                </v-col>
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
import ReviewModal from "@/components/ReviewModal.vue";
export default {
  components: {
    ReviewModal,
  },
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
      nestedModalOpen: false, // 하위 모달 창 상태
    };
  },
  methods: {
    openNestedModal(detail) {
      this.selectedDetail = detail;
      console.log(this.selectedDetail);
      this.nestedModalOpen = true; // 하위 모달 창 열기
    },
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
        console.log(this.orderList);
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
          console.log(this.orderDetailList);
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
  width: 70%;
}
</style>
