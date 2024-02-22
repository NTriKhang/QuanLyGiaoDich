import React from "react";

import banner from "../../images/Banner.jpg";

const Banner = () => {
    return (
        <div className={"col-6"}>
            <img 
                src={banner} 
                alt="Banner"
                className="banner_img"
                style={{width: 500}} />
        </div>
    )
};

export default Banner;