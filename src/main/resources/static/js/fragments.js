document.addEventListener('DOMContentLoaded', function() {
    // 1. Обработка владельцев (owners)
    const addButton = document.getElementById('add-owner-btn');
    const ownersContainer = document.getElementById('owners-container');

    if (addButton && ownersContainer) {
        addButton.addEventListener('click', function() {
            const newIndex = ownersContainer.children.length;
            const newOwnerHtml = `
                <div class="input-group mb-2 owner-item">
                    <input
                        type="text"
                        class="form-control"
                        name="owners[${newIndex}]"
                        placeholder="Owner name"
                        required
                    />
                    <button type="button" class="btn btn-outline-danger remove-owner-btn">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            `;
            ownersContainer.insertAdjacentHTML('beforeend', newOwnerHtml);
        });

        ownersContainer.addEventListener('click', function(e) {
            if (e.target.closest('.remove-owner-btn') && ownersContainer.children.length > 1) {
                e.target.closest('.owner-item').remove();
                // Переиндексация оставшихся полей
                Array.from(ownersContainer.children).forEach((item, index) => {
                    item.querySelector('input').name = `car.owners[${index}].name`;
                });
            }
        });
    }

    // 2. Обработка фотографий (единый обработчик)
    const photoInput = document.getElementById('photo-files');
    const photoContainer = document.getElementById('photoPreviewContainer');
    const maxFiles = 5;

    if (photoInput && photoContainer) {
        photoInput.addEventListener('change', function(e) {
            photoContainer.innerHTML = '';
            const files = e.target.files;

            // Валидация
            if (files.length > maxFiles) {
                alert(`Maximum ${maxFiles} photos allowed.`);
                e.target.value = '';
                return;
            }

            // Превью и кнопки удаления
            Array.from(files).forEach((file, index) => {
                if (!file.type.startsWith('image/')) return;

                const reader = new FileReader();
                reader.onload = function(e) {
                    const col = document.createElement('div');
                    col.className = 'col-6 col-md-4 col-lg-3';
                    col.innerHTML = `
                        <div class="card">
                            <img src="${e.target.result}" class="card-img-top img-thumbnail" alt="Preview">
                            <div class="card-body p-2">
                                <button
                                    type="button"
                                    class="btn btn-sm btn-outline-danger w-100 remove-photo-btn"
                                    data-index="${index}"
                                >
                                    <i class="bi bi-trash"></i> Remove
                                </button>
                            </div>
                        </div>
                    `;
                    photoContainer.appendChild(col);
                };
                reader.readAsDataURL(file);
            });
        });

        // 3. Удаление фотографий из превью
        photoContainer.addEventListener('click', function(e) {
            if (e.target.closest('.remove-photo-btn')) {
                const index = e.target.closest('.remove-photo-btn').getAttribute('data-index');
                const input = document.getElementById('photo-files');

                // Обновляем FileList
                const newFiles = Array.from(input.files).filter((_, i) => i != index);
                const dataTransfer = new DataTransfer();
                newFiles.forEach(file => dataTransfer.items.add(file));
                input.files = dataTransfer.files;

                // Обновляем превью
                input.dispatchEvent(new Event('change'));
            }
        });
    }
});

// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        })
})()


// Listing page
document.addEventListener('DOMContentLoaded', function() {
    const gridViewBtn = document.getElementById('gridViewBtn');
    const listViewBtn = document.getElementById('listViewBtn');
    const postsContainer = document.getElementById('postsContainer');
    const gridViewContent = document.querySelector('.grid-view-content');
    const listViewContent = document.querySelector('.list-view-content');

    if (gridViewBtn && listViewBtn && postsContainer && gridViewContent && listViewContent) {
        gridViewBtn.addEventListener('click', function() {
            this.classList.add('active');
            listViewBtn.classList.remove('active');
            postsContainer.classList.remove('list-view');
            postsContainer.classList.add('grid-view');
            gridViewContent.classList.remove('d-none');
            listViewContent.classList.add('d-none');
        });

        listViewBtn.addEventListener('click', function() {
            this.classList.add('active');
            gridViewBtn.classList.remove('active');
            postsContainer.classList.remove('grid-view');
            postsContainer.classList.add('list-view');
            gridViewContent.classList.add('d-none');
            listViewContent.classList.remove('d-none');
        });
    }

    // Добавляем обработчик клика для всех карточек
    const postCards = document.querySelectorAll('.post-card');

    postCards.forEach(function(card) {
        card.addEventListener('click', function(e) {
            // Получаем URL из data-атрибута
            const url = this.getAttribute('data-url');
            if (url) {
                window.location.href = url;
            }
        });

        // Добавляем hover эффект через Bootstrap классы
        card.addEventListener('mouseenter', function() {
            this.classList.add('shadow');
        });

        card.addEventListener('mouseleave', function() {
            this.classList.remove('shadow');
        });
    });
});


// Like/Subscribe button functionality
document.addEventListener('DOMContentLoaded', function() {
    const likeBtn = document.getElementById('likeBtn');
    if (likeBtn) {
        likeBtn.addEventListener('click', function() {

            const postId = this.getAttribute('data-post-id');
            const icon = this.querySelector('i');
            const text = this.querySelector('span:not(.badge)');
            const badge = this.querySelector('.badge');

            fetch('/posts/' + postId + '/subscribe', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(response => {
                if (response.redirected) {
                    // Если сервер вернул редирект, обновляем страницу
                    window.location.href = response.url;
                } else {
                    return response.json();
                }
            })
            .then(data => {
                if(data && data.liked) {
                    icon.classList.remove('bi-heart');
                    icon.classList.add('bi-heart-fill', 'text-danger');
                    text.textContent = 'Subscribed';
                    badge.textContent = data.likesCount;
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }
});

// Contact form submission
document.addEventListener('DOMContentLoaded', function() {
    const contactForm = document.getElementById('contactForm');
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {

            e.preventDefault();
            const message = document.getElementById('messageText').value;
            const postId = document.getElementById('contactBtn').getAttribute('data-post-id');

            fetch('/posts/' + postId + '/contact', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: JSON.stringify({ message: message })
            })
            .then(response => {
                if(response.ok) {
                    alert('Message sent successfully!');
                    bootstrap.Modal.getInstance(document.getElementById('contactModal')).hide();
                } else {
                    alert('Failed to send message. Please try again.');
                }
            });
        });
    }
});

//deleteModal
document.addEventListener('DOMContentLoaded', function() {
    const deleteModalElement = document.getElementById('deletePostModal');
    if (deleteModalElement) {
        const deleteModal = new bootstrap.Modal(deleteModalElement);
        const deleteForm = document.getElementById('deletePostForm');

        // Обработка клика по всем кнопкам удаления
        document.querySelectorAll('.btn-delete-post').forEach(btn => {
            btn.addEventListener('click', function() {
                const postId = this.getAttribute('data-post-id');
                deleteForm.action = `/posts/${postId}/delete`; // Обновляем URL формы
                deleteModal.show(); // Показываем модальное окно
            });
        });
    }
});


        // Notifications functionality
        document.addEventListener('DOMContentLoaded', function() {
            // Initialize modal manually
            const notificationsBtn = document.getElementById('notificationsBtn');
            const notificationsModal = document.getElementById('notificationsModal');

            if (notificationsBtn && notificationsModal) {
                const modal = new bootstrap.Modal(notificationsModal);

                notificationsBtn.addEventListener('click', function() {
                    modal.show();
                });
            }

            // Show delete button on hover
            document.querySelectorAll('.notification-item').forEach(item => {
                const deleteBtn = item.querySelector('.delete-notification-btn');

                if (deleteBtn) {
                    item.addEventListener('mouseenter', function() {
                        deleteBtn.style.display = 'inline-block';
                    });

                    item.addEventListener('mouseleave', function() {
                        deleteBtn.style.display = 'none';
                    });
                }
            });

            // Handle delete notification
            document.querySelectorAll('.delete-notification-btn').forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const notificationId = this.getAttribute('data-notification-id');
                    deleteNotification(notificationId);
                });
            });
        });

        function deleteNotification(notificationId) {
           console.log('deleteNotification called with ID:', notificationId);
           if (confirm('Are you sure you want to delete this notification?')) {
               console.log('User confirmed deletion');

               const formData = new FormData();
               formData.append('_method', 'delete');

               fetch(`/notifications/${notificationId}`, {
                   method: 'POST',
                   body: formData,
                   headers: {
                       'X-Requested-With': 'XMLHttpRequest'
                   }
               })
               .then(response => {
                   console.log('Delete response:', response);
                   if (response.ok) {
                       console.log('Deletion successful');
                       // Remove notification from DOM
                       const notificationItem = document.querySelector(`[data-notification-id="${notificationId}"]`);
                       if (notificationItem) {
                           notificationItem.remove();
                           console.log('Notification item removed from DOM');
                       }

                       // Update notification count
                       updateNotificationCount();

                       // Show empty state if no notifications left
                       const remainingNotifications = document.querySelectorAll('.notification-item');
                       if (remainingNotifications.length === 0) {
                           console.log('No notifications left, reloading page');
                           location.reload();
                       }
                   } else {
                       console.error('Delete failed with status:', response.status);
                       alert('Error deleting notification');
                   }
               })
               .catch(error => {
                   console.error('Error during deletion:', error);
                   alert('Error deleting notification');
               });
           } else {
               console.log('User cancelled deletion');
           }
        }

        function updateNotificationCount() {
            const remainingNotifications = document.querySelectorAll('.notification-item').length;
            const badge = document.querySelector('.badge');
            if (badge) {
                if (remainingNotifications === 0) {
                    badge.style.display = 'none';
                } else {
                    badge.textContent = remainingNotifications;
                }
            }
        }

        function markAllAsRead() {
            // This function can be implemented to mark all notifications as read
            // For now, it just closes the modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('notificationsModal'));
            if (modal) {
                modal.hide();
            }
        }