<template>
    <v-dialog v-model="dialog" persistent max-width="600px">
      <v-card>
        <v-card-title>
          <span>아이템 상세 설명 입력</span>
        </v-card-title>
        <v-card-text>
          <text-editor v-model="localDetail" />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="saveDetail">저장하기</v-btn>
          <v-btn color="red" text @click="close">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>

  <script>
  import TextEditor from "@/components/TextEditor.vue"; // TextEditor 컴포넌트 경로 확인 필요
  
  export default {
    components: {
      TextEditor,
    },
    props: {
      value: Boolean,
      detail: String,
    },
    emits: ['update:value', 'update:detail'],
    data() {
      return {
        dialog: this.value,
        localDetail: this.detail, // 로컬 상태로 detail을 관리
      };
    },
    watch: {
      value(val) {
        this.dialog = val;
      },
      dialog(val) {
        this.$emit('update:value', val);
      },
      detail(newVal) {
        this.localDetail = newVal;
      }
    },
    methods: {
      close() {
        this.$emit('update:value', false);
      },
      saveDetail() {
        this.$emit('update:detail', this.localDetail); // 부모 컴포넌트에 detail 업데이트를 알림
        this.close();
      },
    },
  }
  </script>