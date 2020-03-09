/*
 * Copyright 2020 Administrator.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package afengine.component.render;

import afengine.core.window.IColor;
import afengine.core.window.IGraphicsTech;
import afengine.part.scene.SceneCamera;

/**
 *
 * @author Administrator
 */
public class RectRenderComponent extends RenderComponent{
    
    private IColor color;
    private int width,height;
    private int arcWidth,arcHeight;
    private boolean fill=false;

    public RectRenderComponent() {
        super();
    }

    public IColor getColor() {
        return color;
    }

    public void setColor(IColor color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }
    public int getArcHeight() {
        return arcHeight;
    }
    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }
    @Override
    protected void render(SceneCamera camera, IGraphicsTech tech) {
        super.renderWidth=this.renderWidth;
        super.renderHeight=this.renderHeight;

        int dx=super.getRenderX(camera);
        int dy=super.getRenderY(camera);
        IColor oldc=tech.getColor();
        tech.setColor(this.color);
        tech.drawRoundRect(dx, dy, width, height, arcWidth, arcHeight,fill);
        tech.setColor(oldc);
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }
    
}
