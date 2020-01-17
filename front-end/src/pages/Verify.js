import React, { Component } from 'react'
import './New.css'
import api from '../services/api'


class Verify extends Component {
    state = {
        image: null,
        name: '',
        imagePreviewUrl: null,
        dados: {
            person:{
                name: '',
                photo: ''
            }
        }
    }

    handleSubmit = e => {
        e.preventDefault()

        const data = new FormData()
        data.append('file', this.state.image)
         api.post('/recognition/verify/multipart', data)    
         .then(res => {
             this.setState({dados: res.data})
          })

    }

    handleImageChange = e => {
        this.setState({image: e.target.files[0]})
        let reader = new FileReader();     
        reader.onloadend = () => {
        this.setState({ imagePreviewUrl: reader.result});
        } 
         reader.readAsDataURL(e.target.files[0])
    }

    render() {
        let nameSpan
        let photoImg
        let preview 

        if (this.state.imagePreviewUrl) {
            preview = (<div ><img src={this.state.imagePreviewUrl} alt="icon" width="80" /> </div>);
        }
        
        if (this.state.dados.person && this.state.dados.person.name) {
            nameSpan = <span>{this.state.dados.person.name}</span>;
          } 

        if (this.state.dados.person && this.state.dados.person.photo) {
            photoImg = <img src={`data:image/jpeg;base64,${this.state.dados.person.photo}`} width="200"></img>;
        } 

        return (
            <div align="center">
                <form id="new-post">
                    <input type="file" onChange={(e) => this.handleImageChange(e)}/>
                    { preview }
                    <button type="submit" onClick={this.handleSubmit}>Enviar</button>
                    <br></br>
                     <div >
                        {photoImg}
                        <h1>{nameSpan}</h1><br></br>
                        <span>{this.state.dados.message}</span><br></br>
                    </div>
                    
                </form>
            </div>
        )
    }
}

export default Verify
