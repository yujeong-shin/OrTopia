<template>
    <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
      <v-card>
        <v-card-title class="headline grey lighten-2 text-center">
          판매목록
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row justify="center">
              <v-col cols="12">
                <v-table>
                  <thead>
                    <tr>
                      <th class="text-center">상품명</th>
                      <th class="text-center">가격</th>
                      <th class="text-center">상태</th>
                      <th class="text-center">등록 일자</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="item in itemList" :key="item.id">
                      <td class="text-center">{{ item.name }}</td>
                      <td class="text-center">{{ item.price }}</td>
                      <td class="text-center">{{ item.status }}</td>
                      <td class="text-center">{{ formatDateTime(item.createdTime) }}</td>
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
      this.getItemList();
    },
    props: {
      dialog: Boolean,
    },
    emits: ["update:dialog"],
    data() {
      return {
        itemList: [],
      };
    },
    methods: {
      updateDialog(value) {
        this.$emit("update:dialog", value);
      },
      close() {
        this.$emit("update:dialog", false);
      },
      async getItemList() {
        const token = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");
        const email = localStorage.getItem("email");
        try {
          const response = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/find_item_email`,
            {
              headers: {
                myEmail: `${email}`,
                Authorization: `Bearer ${token}`,
                "X-Refresh-Token": `${refreshToken}`,
              },
            }
          );
          this.itemList = response.data.result;
          console.log("response : ");
          console.log(response);
          console.log("response.data : ");
          console.log(response.data);
          console.log("itemList : ");
          console.log(this.itemList);
        } catch (error) {
          console.error(" 실패했습니다: ", error);
        }
      },
      formatDateTime(dateTime) {
        if (!dateTime) return '';
        return dateTime.replace("T", " ");
        },
    },
  };
  </script>
  