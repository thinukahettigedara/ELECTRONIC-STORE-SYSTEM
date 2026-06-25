// ===== NANOLAP - Main JavaScript =====
const API = 'http://localhost:8080/api';

// ===== CART MANAGEMENT =====
let cart = JSON.parse(localStorage.getItem('nanolap_cart') || '[]');

function saveCart() {
  localStorage.setItem('nanolap_cart', JSON.stringify(cart));
  updateCartBadge();
}

function updateCartBadge() {
  const total = cart.reduce((sum, i) => sum + i.qty, 0);
  document.querySelectorAll('.cart-badge').forEach(b => {
    b.textContent = total;
    b.style.display = total > 0 ? 'flex' : 'none';
  });
}

function addToCart(product) {
  const existing = cart.find(i => i.id === product.id);
  if (existing) {
    existing.qty++;
  } else {
    cart.push({ ...product, qty: 1 });
  }
  saveCart();
  showToast(`✅ ${product.name.substring(0, 30)}... added to cart!`);
}

function removeFromCart(id) {
  cart = cart.filter(i => i.id !== id);
  saveCart();
}

function updateQty(id, delta) {
  const item = cart.find(i => i.id === id);
  if (item) {
    item.qty += delta;
    if (item.qty <= 0) removeFromCart(id);
    else saveCart();
  }
}

// ===== TOAST =====
function showToast(message, isError = false) {
  const toast = document.getElementById('toast') || createToast();
  const icon = toast.querySelector('.toast-icon');
  const text = toast.querySelector('.toast-text');
  icon.textContent = isError ? '❌' : '✅';
  text.textContent = message;
  toast.style.borderColor = isError ? 'rgba(255,45,120,0.3)' : 'rgba(0,255,136,0.3)';
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 3000);
}

function createToast() {
  const t = document.createElement('div');
  t.id = 'toast'; t.className = 'toast';
  t.innerHTML = '<span class="toast-icon">✅</span><span class="toast-text"></span>';
  document.body.appendChild(t);
  return t;
}

// ===== LOADING SCREEN =====
function hideLoading() {
  const ls = document.getElementById('loadingScreen');
  if (ls) {
    setTimeout(() => {
      ls.style.opacity = '0';
      setTimeout(() => ls.remove(), 500);
    }, 1000);
  }
}

// ===== FORMAT PRICE =====
function formatPrice(price) {
  return 'Rs. ' + Number(price).toLocaleString('en-LK');
}

// ===== STAR RATING =====
function renderStars(rating) {
  const full = Math.floor(rating);
  const half = rating % 1 >= 0.5 ? 1 : 0;
  const empty = 5 - full - half;
  return '★'.repeat(full) + (half ? '½' : '') + '☆'.repeat(empty);
}

// ===== PRODUCT CARD HTML =====
function productCardHTML(p) {
  const discount = p.originalPrice
    ? Math.round((1 - p.price / p.originalPrice) * 100) : 0;
  return `
    <div class="product-card" onclick="viewProduct(${p.id})">
      ${discount > 0 ? `<span class="product-badge badge-sale">-${discount}%</span>` : ''}
      <div class="product-img-wrap">
        <img src="${p.imageUrl || 'https://via.placeholder.com/300x200/131320/00d4ff?text=No+Image'}"
             alt="${p.name}" loading="lazy" onerror="this.src='https://via.placeholder.com/300x200/131320/00d4ff?text=No+Image'">
      </div>
      <div class="product-info">
        <div class="product-brand">${p.brand || ''}</div>
        <div class="product-name">${p.name}</div>
        <div class="product-rating">
          <span class="stars">${renderStars(p.rating || 0)}</span>
          <span class="rating-count">(${(p.reviewCount || 0).toLocaleString()})</span>
        </div>
        <div class="product-price">
          <span class="price-current">${formatPrice(p.price)}</span>
          ${p.originalPrice ? `<span class="price-original">${formatPrice(p.originalPrice)}</span>` : ''}
          ${discount > 0 ? `<span class="price-discount">SAVE ${discount}%</span>` : ''}
        </div>
        <div class="product-footer">
          <button class="btn-addcart" onclick="event.stopPropagation(); handleAddCart(${p.id})">
            🛒 Add to Cart
          </button>
          <button class="btn-wish" onclick="event.stopPropagation(); wishlistToggle(${p.id})">♡</button>
        </div>
      </div>
    </div>`;
}

// ===== STORE PRODUCTS GLOBALLY =====
let allProducts = [];

async function handleAddCart(id) {
  const product = allProducts.find(p => p.id === id);
  if (product) addToCart(product);
}

function wishlistToggle(id) {
  showToast('❤️ Added to wishlist!');
}

function viewProduct(id) {
  const product = allProducts.find(p => p.id === id);
  if (!product) return;

  const modal = document.getElementById('productModal');
  if (!modal) return;

  const discount = product.originalPrice
    ? Math.round((1 - product.price / product.originalPrice) * 100) : 0;

  modal.querySelector('.modal-product-img').src = product.imageUrl || '';
  modal.querySelector('.modal-product-brand').textContent = product.brand || '';
  modal.querySelector('.modal-product-name').textContent = product.name;
  modal.querySelector('.modal-product-stars').textContent = renderStars(product.rating || 0);
  modal.querySelector('.modal-product-reviews').textContent = `(${(product.reviewCount || 0).toLocaleString()} reviews)`;
  modal.querySelector('.modal-product-price').textContent = formatPrice(product.price);
  modal.querySelector('.modal-product-desc').textContent = product.description || '';
  modal.querySelector('.modal-product-stock').textContent = product.stockQuantity > 0 ? `✅ In Stock (${product.stockQuantity})` : '❌ Out of Stock';
  modal.querySelector('.modal-addcart').onclick = () => { addToCart(product); modal.classList.remove('open'); };
  modal.classList.add('open');
}

// ===== NAVBAR MOBILE =====
function initNav() {
  const hamburger = document.querySelector('.hamburger');
  const navLinks = document.querySelector('.nav-links');
  if (hamburger && navLinks) {
    hamburger.addEventListener('click', () => navLinks.classList.toggle('open'));
  }

  // Active link
  const current = window.location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.nav-links a').forEach(a => {
    if (a.getAttribute('href') === current || a.getAttribute('href') === './' + current) {
      a.classList.add('active');
    }
  });
}

// ===== INIT =====
document.addEventListener('DOMContentLoaded', () => {
  updateCartBadge();
  initNav();
  hideLoading();

  // Close modals
  document.querySelectorAll('.modal-close, .modal-overlay').forEach(el => {
    el.addEventListener('click', (e) => {
      if (e.target === el) e.target.closest('.modal-overlay')?.classList.remove('open');
      e.target.closest('.modal-overlay')?.classList.remove('open');
    });
  });
});
