<template>
  <div v-if="editor" class="d-flex justify-start align-center">
    <!-- 볼드 -->
    <button
      @click="editor.chain().focus().toggleBold().run()"
      :disabled="!editor.can().chain().focus().toggleBold().run()"
      :class="{ 'is-active': editor.isActive('bold') }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-bold</v-icon>
    </button>

    <!-- 이미지 추가 -->
    <button @click="onImageAdd" class="mx-2">
      <v-icon color="orange">mdi-image</v-icon>
    </button>

    <!-- 이탤릭 -->
    <button
      @click="editor.chain().focus().toggleItalic().run()"
      :disabled="!editor.can().chain().focus().toggleItalic().run()"
      :class="{ 'is-active': editor.isActive('italic') }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-italic</v-icon>
    </button>

    <!-- 취소선 -->
    <button
      @click="editor.chain().focus().toggleStrike().run()"
      :disabled="!editor.can().chain().focus().toggleStrike().run()"
      :class="{ 'is-active': editor.isActive('strike') }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-strikethrough-variant</v-icon>
    </button>

    <!-- 제목 1 -->
    <button
      @click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
      :class="{ 'is-active': editor.isActive('heading', { level: 1 }) }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-header-1</v-icon>
    </button>

    <!-- 제목 2 -->
    <button
      @click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
      :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-header-2</v-icon>
    </button>

    <!-- 제목 3 -->
    <button
      @click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
      :class="{ 'is-active': editor.isActive('heading', { level: 3 }) }" class="mx-2"
    >
      <v-icon color="orange">mdi-format-header-3</v-icon>
    </button>

    <!-- 실행 취소 -->
    <button
      @click="editor.chain().focus().undo().run()"
      :disabled="!editor.can().chain().focus().undo().run()" class="mx-2"
    >
      <v-icon color="orange">mdi-undo-variant</v-icon>
    </button>

    <!-- 다시 실행 -->
    <button
      @click="editor.chain().focus().redo().run()"
      :disabled="!editor.can().chain().focus().redo().run()" class="mx-2"
    >
      <v-icon color="orange">mdi-redo-variant</v-icon>
    </button>
  </div>
  <input type="file" ref="fileInput" accept="image/*" @change="handleFileChange" style="display: none" />
  <!-- 에디터 영역 -->
  <editor-content :editor="editor" style="height: 90%" />
</template>
  
  <script>
  import StarterKit from "@tiptap/starter-kit";
  import Image from "@tiptap/extension-image"; // Image 확장 기능 import
  import { Editor, EditorContent } from "@tiptap/vue-3";
  
  export default {
    components: {
      EditorContent,
    },
  
    props: {
      modelValue: {
          type: String,
          default: "",
      },
    },
  
    emits: ["update:modelValue"],
  
    data() {
      return {
        editor: null,
      };
    },
  
    watch: {
      modelValue(value) {
        if (this.editor && this.editor.getHTML() !== value) {
          this.editor.commands.setContent(value, false);
        }
      },
    },
  
    mounted() {
      this.editor = new Editor({
        extensions: [
          StarterKit,
          Image, // Editor에 Image 확장 기능 추가
        ],
        content: this.modelValue,
        onUpdate: ({ editor }) => {
          this.$emit("update:modelValue", editor.getHTML());
        },
      });
    },
  
    beforeUnmount() {
      if (this.editor) {
        this.editor.destroy();
      }
    },
  
    methods: {
      addImage() {
        this.$refs.fileInput.click();
      },
      onImageAdd() {
      this.$refs.fileInput.click(); // 숨겨진 파일 입력 요소를 클릭하도록 함
    },
    handleFileChange(event) {
      const files = event.target.files;
      if (files.length === 0) {
        return; // 파일이 선택되지 않은 경우 아무 작업도 수행하지 않음
      }
      
      // 선택된 첫 번째 파일을 처리
      const file = files[0];
      
      // FileReader를 사용하여 파일을 읽음
      const reader = new FileReader();
      reader.onload = (e) => {
        const src = e.target.result; // 읽어들인 파일의 내용(데이터 URL)
        
        // 에디터에 이미지 삽입
        this.editor.chain().focus().setImage({ src }).run();
      };
      reader.readAsDataURL(file); // 파일을 데이터 URL로 읽음

      // 파일 입력 요소를 초기화하여 다음 선택을 위해 준비
      this.$refs.fileInput.value = '';
    },
  },
};
</script>
  
  <style lang="scss">
  .ProseMirror {
    height: 100%;
    border: 1px solid black;
    border-radius: 10px;
    padding: 6px;
    overflow: scroll;
  
    > * + * {
      margin-top: 0.75em;
    }
  
    ul,
    ol {
      padding: 0 1rem;
    }
  
    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      line-height: 1.1;
    }
  
    code {
      background-color: rgba(#616161, 0.1);
      color: #616161;
    }
  
    pre {
      background: #0d0d0d;
      color: #fff;
      font-family: "JetBrainsMono", monospace;
      padding: 0.75rem 1rem;
      border-radius: 0.5rem;
  
      code {
        color: inherit;
        padding: 0;
        background: none;
        font-size: 0.8rem;
      }
    }
  
    img {
      max-width: 100%;
      height: auto;
    }
  
    blockquote {
      padding-left: 1rem;
      border-left: 2px solid rgba(#0d0d0d, 0.1);
    }
  
    hr {
      border: none;
      border-top: 2px solid rgba(#0d0d0d, 0.1);
      margin: 2rem 0;
    }
  }
  .ProseMirror {
    min-height: 400px; /* 최소 높이 조정 */
    font-size: 18px; /* 글자 크기 조정 */
    border: 1px solid #ccc; /* 경계선 색상을 더 부드러운 색으로 변경 */
    border-radius: 4px; /* 경계선 둥근 처리 */
    padding: 20px; /* 안쪽 여백 조정 */
    overflow: auto; /* 내용이 넘칠 경우 스크롤바 표시 */
  
    > * + * {
      margin-top: 1em;
    }
  
    ul, ol {
      padding: 0 1rem;
    }
  
    /* 기타 스타일 조정 */
  }
  </style>