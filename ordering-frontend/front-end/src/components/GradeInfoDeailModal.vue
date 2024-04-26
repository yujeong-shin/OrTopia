<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1000px">
    <v-card>
      <v-container>
        <v-img
          @click="showGradeInfoDeailModal = true"
          src="@/assets/gradeInfoDetail.png"
          class="mb-3"
          style="height: 100%; width: 100%; object-fit: contain"
        ></v-img>
      </v-container>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  components: {},
  created() {},
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      showModal: false,
    };
  },
  methods: {
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
  },
};
</script>
