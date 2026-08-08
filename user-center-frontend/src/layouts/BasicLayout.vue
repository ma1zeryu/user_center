<script setup>
import FooterView from '@/views/FooterView.vue';
import GlobalHeader from '@/components/GlobalHeader.vue';
import { getCurrentUser, userLogin } from '@/api/user.js';

userLogin({ userAccount: '玉米排骨汤', userPassword: '12345678' });
getCurrentUser().then((user) => {
  console.log(user);
});
</script>

<template>
  <div id="basicLayout">
    <a-layout>
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <FooterView />
    </a-layout>
  </div>
</template>

<style scoped>
#basicLayout .content {
  padding: 20px;
  margin-bottom: 20px;
  background: linear-gradient(to right, #fefefe, #fff);
}

#basicLayout .header {
  background: white;
  margin-bottom: 16px;
  color: unset;
  padding-inline: 20px;
}
</style>
