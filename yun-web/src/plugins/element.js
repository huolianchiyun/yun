import Vue from 'vue'
// 从element-ui中导入相应的组件，按需导入
import {
  Button, Form, FormItem, Input, Message, Container, Header, Aside, Main, MessageBox,
  Menu, Submenu, MenuItem, Breadcrumb, BreadcrumbItem, Card, Row, Col, Table,
  TableColumn, Pagination, Tooltip, Switch, Dialog, Select, Option, DatePicker, TimePicker,
  Checkbox, CheckboxGroup, Radio, RadioGroup, Collapse, CollapseItem, Steps, Step, Divider,
  Dropdown, DropdownItem, DropdownMenu, Scrollbar, ColorPicker, Notification
} from 'element-ui'

// 注册为全局可用的插件
Vue.use(Button)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Input)
Vue.use(Container)
Vue.use(Header)
Vue.use(Aside)
Vue.use(Menu)
Vue.use(Submenu)
Vue.use(MenuItem)
Vue.use(Main)
Vue.use(Breadcrumb)
Vue.use(BreadcrumbItem)
Vue.use(Card)
Vue.use(Row)
Vue.use(Col)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(Pagination)
Vue.use(Tooltip)
Vue.use(Switch)
Vue.use(Dialog)
Vue.use(Select)
Vue.use(Option)
Vue.use(DatePicker)
Vue.use(TimePicker)
Vue.use(Checkbox)
Vue.use(CheckboxGroup)
Vue.use(Radio)
Vue.use(RadioGroup)
Vue.use(Collapse)
Vue.use(CollapseItem)
Vue.use(Steps)
Vue.use(Step)
Vue.use(Divider)
Vue.use(DropdownItem)
Vue.use(Dropdown)
Vue.use(DropdownMenu)
Vue.use(Scrollbar)
Vue.use(ColorPicker)

/** 重置message，防止重复点击重复弹出message弹框 */
let messageInstance = null
const resetMessage = (options) => {
  if (messageInstance) {
    messageInstance.close()
  }
  messageInstance = Message(options)
}
['error', 'success', 'info', 'warning'].forEach(type => {
  resetMessage[type] = options => {
    if (typeof options === 'string') {
      options = {
        message: options
      }
    }
    options.type = type
    return resetMessage(options)
  }
})
// 全局挂载到Vue的原型对象上，这就vue组件就可以直接this.$message使用了
Vue.prototype.$message = resetMessage
Vue.prototype.$confirm = MessageBox.confirm
Vue.prototype.$notify = Notification
