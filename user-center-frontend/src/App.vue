<script setup>
import BasicLayout from '@/layouts/BasicLayout.vue';
import { useLoginUserStore } from '@/stores/useLoginUserStore.js';

const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();
</script>

<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style scoped></style>
