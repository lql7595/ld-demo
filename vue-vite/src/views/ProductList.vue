<template>
  <AppLayout>
    <div class="product-container">
      <div class="product-header">
        <h1>商品管理</h1>
        <button class="add-btn" @click="showAddModal = true">
          <span>+</span> 添加商品
        </button>
      </div>
    
    <!-- 商品列表 -->
    <div class="product-list">
      <div v-if="loading" class="loading">
        加载中...
      </div>
      
      <div v-else-if="products.length === 0" class="empty-state">
        暂无商品，点击上方按钮添加商品
      </div>
      
      <div v-else class="product-grid">
        <div 
          v-for="product in products" 
          :key="product.id" 
          class="product-card"
        >
          <div class="product-info">
            <div class="product-id">ID: {{ product.id }}</div>
            <div class="product-name">{{ product.productName }}</div>
          </div>
          <div class="product-actions">
            <button 
              class="edit-btn" 
              @click="handleEdit(product)"
              title="编辑"
            >
              编辑
            </button>
            <button 
              class="delete-btn" 
              @click="handleDelete(product.id)"
              title="删除"
            >
              删除
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加/编辑商品模态框 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click="closeModal">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>{{ showEditModal ? '编辑商品' : '添加商品' }}</h3>
          <button class="close-btn" @click="closeModal">&times;</button>
        </div>
        
        <div class="modal-body">
          <div class="form-group">
            <label>商品名称</label>
            <input 
              v-model="formData.productName" 
              type="text" 
              placeholder="请输入商品名称"
              :disabled="submitting"
            />
          </div>
        </div>
        
        <div class="modal-footer">
          <button 
            class="cancel-btn" 
            @click="closeModal"
            :disabled="submitting"
          >
            取消
          </button>
          <button 
            class="submit-btn" 
            @click="handleSubmit"
            :disabled="submitting || !formData.productName.trim()"
          >
            {{ submitting ? '提交中...' : (showEditModal ? '更新' : '添加') }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- 确认删除模态框 -->
    <div v-if="showDeleteModal" class="modal-overlay" @click="showDeleteModal = false">
      <div class="modal delete-modal" @click.stop>
        <div class="modal-header">
          <h3>确认删除</h3>
        </div>
        
        <div class="modal-body">
          <p>确定要删除这个商品吗？此操作不可撤销。</p>
        </div>
        
        <div class="modal-footer">
          <button 
            class="cancel-btn" 
            @click="showDeleteModal = false"
            :disabled="deleting"
          >
            取消
          </button>
          <button 
            class="delete-confirm-btn" 
            @click="confirmDelete"
            :disabled="deleting"
          >
            {{ deleting ? '删除中...' : '确认删除' }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- 错误提示 -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
  </AppLayout>
</template>

<script>
import { productAPI } from '../api'
import AppLayout from '../components/AppLayout.vue'

export default {
  name: 'ProductList',
  components: {
    AppLayout
  },
  data() {
    return {
      products: [],
      loading: false,
      submitting: false,
      deleting: false,
      errorMessage: '',
      showAddModal: false,
      showEditModal: false,
      showDeleteModal: false,
      formData: {
        productName: '',
        id: null
      },
      editingProduct: null,
      deletingProductId: null
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    async loadProducts() {
      this.loading = true
      this.errorMessage = ''
      
      try {
        const response = await productAPI.getProducts()
        this.products = response.data.records || []
      } catch (error) {
        this.errorMessage = '加载商品列表失败'
        console.error('Load products error:', error)
      } finally {
        this.loading = false
      }
    },
    
    handleEdit(product) {
      this.editingProduct = product
      this.formData.productName = product.productName
      this.showEditModal = true
    },
    
    handleDelete(productId) {
      this.deletingProductId = productId
      this.showDeleteModal = true
    },
    
    async handleSubmit() {
      this.submitting = true
      this.errorMessage = ''
      
      try {
        if (this.showEditModal) {
          // 更新商品
          var params = {
            id: this.editingProduct.id,
            newPrdName: this.formData.productName
          }
          
          await productAPI.updateProduct(params)
          
        } else {
          // 添加商品
          const response = await productAPI.createProduct(this.formData)
          this.products.push(response.data)
        }
        
        this.closeModal()
        this.loadProducts()
      } catch (error) {
        this.errorMessage = this.showEditModal ? '更新商品失败' : '添加商品失败'
        console.error('Submit error:', error)
      } finally {
        this.submitting = false
      }
    },
    
    async confirmDelete() {
      this.deleting = true
      this.errorMessage = ''
      
      try {
        var params = {
          id: this.deletingProductId
        }
        await productAPI.deleteProduct(params)
        this.loadProducts()
        this.showDeleteModal = false
      } catch (error) {
        this.errorMessage = '删除商品失败'
        console.error('Delete error:', error)
      } finally {
        this.deleting = false
      }
    },
    
    closeModal() {
      this.showAddModal = false
      this.showEditModal = false
      this.formData.name = ''
      this.editingProduct = null
      this.errorMessage = ''
    }
  }
}
</script>

<style scoped>
.product-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.product-header h1 {
  margin: 0;
  color: #333;
  font-size: 28px;
}

.add-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: background-color 0.3s ease;
}

.add-btn:hover {
  background: #0056b3;
}

.add-btn span {
  font-size: 18px;
  font-weight: bold;
}

.loading, .empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
  font-size: 16px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: box-shadow 0.3s ease;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-info {
  flex: 1;
}

.product-id {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.product-actions {
  display: flex;
  gap: 8px;
}

.edit-btn, .delete-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.3s ease;
}

.edit-btn {
  background: #28a745;
  color: white;
}

.edit-btn:hover {
  background: #218838;
}

.delete-btn {
  background: #dc3545;
  color: white;
}

.delete-btn:hover {
  background: #c82333;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px;
  border-top: 1px solid #e0e0e0;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s ease;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
}

.cancel-btn, .submit-btn, .delete-confirm-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.cancel-btn {
  background: #6c757d;
  color: white;
}

.cancel-btn:hover:not(:disabled) {
  background: #5a6268;
}

.submit-btn {
  background: #007bff;
  color: white;
}

.submit-btn:hover:not(:disabled) {
  background: #0056b3;
}

.delete-confirm-btn {
  background: #dc3545;
  color: white;
}

.delete-confirm-btn:hover:not(:disabled) {
  background: #c82333;
}

.cancel-btn:disabled, .submit-btn:disabled, .delete-confirm-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.delete-modal .modal-body {
  text-align: center;
}

.delete-modal .modal-body p {
  margin: 0;
  color: #666;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  padding: 12px;
  border-radius: 6px;
  margin-top: 20px;
  text-align: center;
  border: 1px solid #f5c6cb;
}
</style> 