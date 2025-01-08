const imageInput = document.getElementById('imageFile');
const imagePreview = document.getElementById('imagePrevire');

// 画像ファイルが更新される度に発生
imageInput.addEventListener('change', () => {
  if (imageInput.files[0]) {
    // プレビューに画像ファイルを設定
    let fileReader = new FileReader();
    fileReader.onload = () => {
      imagePreview.innerHTML = `<img src="${fileReader.result}" class="mb-3">`;
    }
    // プレビューを空にする
    fileReader.readAsDataURL(imageInput.files[0]);
  } else {
    imagePreview.innerHTML = '';
  }
})
