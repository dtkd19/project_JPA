console.log("board detail js in ");


const cantAddBtn = document.getElementById('cantAddBtn');
if (cantAddBtn) {
    cantAddBtn.addEventListener('click', () => {
        alert("로그인 후 댓글을 등록하실 수 있습니다.");
    });
}

// 인증된 사용자의 댓글 버튼 처리
const cmtAddBtn = document.getElementById('cmtAddBtn');
if (cmtAddBtn) {
    cmtAddBtn.addEventListener('click', () => {
        const cmtText = document.getElementById('cmtText');
        const cmtWriter = document.getElementById('cmtWriter');

        let cmtData = {
            bno: bnoVal,
            writer: cmtWriter.innerText,
            content: cmtText.value
        };

        postCommentToServer(cmtData).then(result => {
            if (result !== "0") {
                alert("댓글 등록 성공");
                cmtText.value = "";
            } else {
                alert("댓글 등록 실패");
            }
            spreadComment(bnoVal);
        });
    });
}


document.addEventListener('click', (e) => {
    if(e.target.id == "moreBtn"){
        let page = parseInt(e.target.dataset.page);
        spreadComment(bnoVal, page);
    }
    if(e.target.classList.contains('mod')){

        let li = e.target.closest('li');
        
        let cmtWriter = li.querySelector('.fw-bold').innerText;
        document.getElementById('cmtWriterMod').innerHTML = cmtWriter;

        let cmtText = li.querySelector('.fw-bold').nextSibling;
        document.getElementById('cmtTextMod').value = cmtText.nodeValue;

        document.getElementById('cmtModBtn').setAttribute("data-cno", li.dataset.cno);
    }
    if(e.target.id == 'cmtModBtn'){
        let cmtData = {
            cno: e.target.dataset.cno,  
            content: document.getElementById('cmtTextMod').value
        }
        console.log(cmtData);
        modifyCommentToServer(cmtData).then(result => {
            if(result != '0'){
                alert("댓글 수정 성공");
            } else{
                alert("댓글 수정 실패");
            }    
            // 모달창 닫기
            document.querySelector('.btn-close').click();
            // 댓글 뿌리기
            spreadComment(bnoVal);
        })
    }
    if(e.target.classList.contains('del')){
        let cno = e.target.closest('li').dataset.cno;
        deleteCommentToServer(cno).then( result => {
            if(result != '0'){
                alert("삭제 성공");
                spreadComment(bnoVal);
            } else{
                alert("삭제 실패");
            }
        })
    }

})


// 댓글 뿌리기
function spreadComment(bno, page = 1){
    getCommentFromServer(bno,page).then(result => {
        console.log(result);
        const ul = document.getElementById('cmtListArea');
        if( result.content.length > 0 ){
            if( page == 1){
                ul.innerHTML = ""; 
            }
            for(let cvo of result.content){
                let li = `<li class="list-group-item" data-cno=${cvo.cno}>`;
                li += `<div class="ms-2 me-auto">`;
                li += `<div class="fw-bold">${cvo.writer}</div>`;
                li += `${cvo.content}`;
                li += `</div>`;
                li += `<span class="badge text-bg-primary rounded-pill">${cvo.regAt}</span>`;
                // 수정 삭제 버튼 추가   
                if (cvo.writer === userNickName || isAdmin){
                    li += `<div class="d-grid gap-2 d-md-flex justify-content-md-end">`;
                    li += `<button type="button" data-cno=${cvo.cno} class="btn btn-outline-warning btn-sm mod" data-bs-toggle="modal" data-bs-target="#myModal">%</button>`;
                    li += `<button type="button" data-cno=${cvo.cno} class="btn btn-outline-danger btn-sm del">X</button>`;
                    li += `</div>`;
                }
                li += `</li>`;
                ul.innerHTML += li;           
            } 
            let moreBtn = document.getElementById('moreBtn');
            if( page < result.totalPages ){
                moreBtn.style.visibility = 'visible';
                moreBtn.dataset.page = page + 1 ; 
            } else{
                moreBtn.style.visibility = 'hidden';
            }
        } else{
            ul.innerHTML = `<li class="list-group-item">Comment List Empty</li>`;
        }
    })

}


// 댓글 등록 
async function postCommentToServer(cmtData) {
    try {
        const url = "/comment/post"
        const config = {
            method: 'post',
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			},
			body: JSON.stringify(cmtData)
        };
        const resp = await fetch(url, config);
        console.log(resp);
        const result = await resp.text(); 
        return result;
    } catch (error) {
        console.log(error);
    }
}


// 댓글 리스트 가져오기
async function getCommentFromServer(bno,page) {
    try {
        const resp = await fetch("/comment/list/"+ bno +"/" + page) 
        const result = await resp.json();
        return result;
    } catch (error) {
        console.log(error);
    }
}

// 댓글 수정
async function modifyCommentToServer(cmtData) {
    try {
        const url = "/comment/modify";
        const config ={
            method : "put",
            headers : {
                'Content-Type' : 'application/json; charset=utf-8'
            },
            body : JSON.stringify(cmtData)
        };
        const resp = await fetch(url,config);
        const result = await resp.text();

       return result; 

    } catch (error) {
        console.log(error);
    }
}

// 댓글 삭제
async function deleteCommentToServer(cno) {
    try{
        const resp = await fetch("/comment/delete/"+ cno) 
        const result = await resp.text();
        return result;

    } catch(error){
        console.log(error);
    }
}