<template>
    <v-dialog v-model="localDialog" persistent max-width="500px">
      <v-card>
        <v-card-title class="headline">아이디 찾기</v-card-title>
        <v-card-text>
          <v-form @submit.prevent="findUserId" ref="form">
            <v-text-field
              label="등록된 이메일 입력"
              v-model="userEmail"
              prepend-icon="mdi-email"
              type="email"
              :rules="emailRules"
              required
              clearable
            ></v-text-field>
            <v-btn type="submit" color="primary" :disabled="!validEmail">아이디 찾기</v-btn>
            <v-btn text color="red" @click="close">닫기</v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    props: {
      dialog: Boolean
    },
    emits: ['update:dialog'],
    data() {
      return {
        localDialog: this.dialog,
        userEmail: '',
        emailRules: [
          v => !!v || '이메일을 입력해주세요.',
          v => /.+@.+\..+/.test(v) || '유효한 이메일 주소를 입력해주세요.'
        ],
      };
    },
    computed: {
      validEmail() {
        return this.emailRules.every(rule => rule(this.userEmail) === true);
      }
    },
    methods: {
      async findUserId() {
        if (!this.validEmail) {
          this.$refs.form.validate();
          return;
        }
        try {
          const formData = new FormData();
          formData.append('email', this.userEmail);
          const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/find-id`, formData, {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          });
          alert(`${response.data}`);
          this.$emit('update:dialog', false);
        } catch (error) {
          console.error('아이디 찾기 실패:', error);
          alert('아이디 찾기에 실패했습니다. 등록된 이메일을 확인해주세요.');
        }
      },
      close() {
        this.$emit('update:dialog', false);
      }
    }
  };
  </script>
  