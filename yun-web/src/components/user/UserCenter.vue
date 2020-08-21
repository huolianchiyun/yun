<template>
  <div>
    <el-dropdown size="medium" @command="handleCommand">
                  <span class="el-dropdown-link">
                    <i :class="loginUser === 'admin' ? 'iconfont icon-guanliyuan1' : 'iconfont icon-denglu'"></i>
                    <i class="iconfont icon-gengduotubiao2"></i>
                  </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="logout">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: 'LoginUser',
  props: ['loginUser'],
  methods: {
    handleCommand (command) {
      if (command === 'logout') {
        this.logout()
      }
    },
    logout () {
      window.sessionStorage.clear()
      this.$store.commit('clearState')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
  div[class~="el-dropdown"] {
    font-size: 16px;
    padding-top: 30px;
    color: #fff;
  }
</style>
