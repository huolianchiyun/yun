<template>
  <div v-if="!item.hidden" class="menu-wrapper">
    <template v-if="isLeafNode(item)">
      <app-link v-if="menuItem.meta" :to="resolvePath(menuItem.path)">
        <el-menu-item :index="resolvePath(menuItem.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <item :icon="menuItem.meta && menuItem.meta.icon" :title="menuItem.meta.title"/>
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title"/>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data () {
    this.menuItem = {}
    return {}
  },
  methods: {
    isLeafNode (node) {
      let children = node.children
      if (children == null) {
        this.menuItem = node
        return true
      }
      return children.filter(item => {
        if (item.path === 'home') {
          this.menuItem = item
        }
        return !item.hidden
      }).length === 0 || this.menuItem.path === 'home'
    },
    resolvePath (routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
