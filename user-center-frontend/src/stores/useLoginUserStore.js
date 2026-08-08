import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getCurrentUser } from '@/api/user';

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref({
    username: '未登录',
  });

  /**
   * 获取用户信息
   * @returns {Promise<void>}
   */
  async function fetchLoginUser() {
    const res = await getCurrentUser();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    }
  }

  /**
   * 不用从远程获取用户信息的情况下
   * @param newLoginUser
   */
  function setLoginUser(newLoginUser) {
    loginUser.value = newLoginUser;
  }

  return {
    loginUser,
    setLoginUser,
    fetchLoginUser,
  };
});
