<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center">
        회원정보 수정
      </v-card-title>
      <v-card>
        <v-card-text>
          <v-form>
            <v-text-field
              label="이메일"
              v-model="member.email"
              prepend-icon="mdi-email"
              type="email"
              :disabled="true"
            ></v-text-field>

            <v-text-field
              label="이름"
              v-model="member.name"
              prepend-icon="mdi-account"
            ></v-text-field>

            <v-text-field
              label="나이"
              v-model="member.age"
              prepend-icon="mdi-calendar"
              type="number"
            ></v-text-field>

            <v-select
              label="성별"
              :items="['MALE', 'FEMALE']"
              v-model="member.gender"
              prepend-icon="mdi-gender-male-female"
            ></v-select>

            <v-text-field
              label="전화번호"
              v-model="member.phoneNumber"
              prepend-icon="mdi-phone"
              type="tel"
            ></v-text-field>
          </v-form>
        </v-card-text>
      </v-card>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="updateMemberInfo">수정</v-btn>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
export default {
  created() {
    this.getMemberInfo();
  },
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      member: {
        email: "",
        name: "",
        age: null,
        gender: "",
        phoneNumber: "",
      },
    };
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
    async getMemberInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member`,
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
    async updateMemberInfo() {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const email = localStorage.getItem("email");
      try {
        await axios.patch(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-member-service/member`,
          this.member, // 수정된 부분: member 객체를 직접 참조
          {
            headers: {
              myEmail: `${email}`,
              Authorization: `Bearer ${token}`,
              "X-Refresh-Token": `${refreshToken}`,
            },
          }
        );
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
      alert("회원정보 수정이 완료되었습니다");
      this.close();
      location.reload();
    },
  },
};
</script>
