package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TextMain

@Composable
fun HeaderBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu Button
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .size(48.dp)
                .testTag("menu_button")
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = TextMain,
                modifier = Modifier.size(24.dp)
            )
        }

        // Brand Logo: Geometric Yantra Triangle + ASWIN BREATH
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("brand_logo")
        ) {
            GeometricLogoIcon(modifier = Modifier.size(24.dp))

            Text(
                text = "ASWIN BREATH",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = TextMain
            )
        }

        // Right Action Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("search_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMain,
                    modifier = Modifier.size(22.dp)
                )
            }

            IconButton(
                onClick = onCartClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("cart_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Shop & Gear",
                    tint = TextMain,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun GeometricLogoIcon(
    modifier: Modifier = Modifier,
    color: Color = TextMain
) {
    // Draws the nested triangular pranayama geometry from the HTML:
    // <path d="M12 2L3 20h18L12 2z"></path>
    // <path d="M12 7l5 10H7l5-10z"></path>
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeWidth = 2.dp.toPx()

        // Outer triangle
        val outerPath = Path().apply {
            moveTo(w * 0.5f, h * (2f / 24f))
            lineTo(w * (3f / 24f), h * (20f / 24f))
            lineTo(w * (21f / 24f), h * (20f / 24f))
            close()
        }
        drawPath(
            path = outerPath,
            color = color,
            style = Stroke(width = strokeWidth)
        )

        // Inner triangle
        val innerPath = Path().apply {
            moveTo(w * 0.5f, h * (7f / 24f))
            lineTo(w * (17f / 24f), h * (17f / 24f))
            lineTo(w * (7f / 24f), h * (17f / 24f))
            close()
        }
        drawPath(
            path = innerPath,
            color = color,
            style = Stroke(width = strokeWidth)
        )
    }
}
