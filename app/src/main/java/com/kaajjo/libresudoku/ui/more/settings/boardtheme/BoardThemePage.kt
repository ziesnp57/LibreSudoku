package com.kaajjo.libresudoku.ui.more.settings.boardtheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.GridGoldenratio
import androidx.compose.material.icons.rounded.GridOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaajjo.libresudoku.R
import com.kaajjo.libresudoku.core.PreferencesConstants
import com.kaajjo.libresudoku.ui.components.AnimatedNavigation
import com.kaajjo.libresudoku.ui.components.PreferenceRow
import com.kaajjo.libresudoku.ui.components.PreferenceRowSwitch
import com.kaajjo.libresudoku.ui.components.collapsing_topappbar.CollapsingTitle
import com.kaajjo.libresudoku.ui.components.collapsing_topappbar.CollapsingTopAppBar
import com.kaajjo.libresudoku.ui.components.collapsing_topappbar.rememberTopAppBarScrollBehavior
import com.kaajjo.libresudoku.ui.more.settings.SelectionDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = AnimatedNavigation::class)
@Composable
fun SettingsBoardTheme(
    viewModel: SettingsBoardThemeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val scrollBehavior = rememberTopAppBarScrollBehavior()
    val positionLines by viewModel.positionLines.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_POSITION_LINES)
    val highlightMistakes by viewModel.highlightMistakes.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_HIGHLIGHT_MISTAKES)
    val boardCrossHighlight by viewModel.crossHighlight.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_BOARD_CROSS_HIGHLIGHT)
    val fontSizeFactor by viewModel.fontSize.collectAsStateWithLifecycle(initialValue = PreferencesConstants.DEFAULT_FONT_SIZE_FACTOR)

    var fontSizeDialog by rememberSaveable {
        mutableStateOf(false)
    }

    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsingTopAppBar(
                collapsingTitle = CollapsingTitle.medium(titleText = stringResource(R.string.board_theme_title)),
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            ) {

            }

            val monetSudokuBoard by viewModel.monetSudokuBoard.collectAsStateWithLifecycle(
                PreferencesConstants.DEFAULT_MONET_SUDOKU_BOARD
            )
            PreferenceRowSwitch(
                title = stringResource(R.string.pref_boardtheme_accent),
                subtitle = stringResource(R.string.pref_boardtheme_accent_subtitle),
                checked = monetSudokuBoard,
                painter = rememberVectorPainter(Icons.Outlined.Palette),
                onClick = {
                    viewModel.updateMonetSudokuBoardSetting(!monetSudokuBoard)
                }
            )

            PreferenceRowSwitch(
                title = stringResource(R.string.pref_position_lines),
                subtitle = stringResource(R.string.pref_position_lines_summ),
                checked = positionLines,
                painter = rememberVectorPainter(Icons.Rounded.GridGoldenratio),
                onClick = { viewModel.updatePositionLinesSetting(!positionLines) }
            )

            PreferenceRowSwitch(
                title = stringResource(R.string.pref_cross_highlight),
                subtitle = stringResource(R.string.pref_cross_highlight_subtitle),
                checked = boardCrossHighlight,
                painter = rememberVectorPainter(Icons.Rounded.GridOn),
                onClick = { viewModel.updateBoardCrossHighlight(!boardCrossHighlight) }
            )
            PreferenceRow(
                title = stringResource(R.string.pref_board_font_size),
                subtitle = when (fontSizeFactor) {
                    0 -> stringResource(R.string.font_size_automatic)
                    1 -> stringResource(R.string.pref_board_font_size_small)
                    2 -> stringResource(R.string.pref_board_font_size_medium)
                    3 -> stringResource(R.string.pref_board_font_size_large)
                    else -> ""
                },
                painter = rememberVectorPainter(Icons.Rounded.FormatSize),
                onClick = { fontSizeDialog = true }
            )
        }

        if (fontSizeDialog) {
            SelectionDialog(
                title = stringResource(R.string.pref_board_font_size),
                selections = listOf(
                    stringResource(R.string.font_size_automatic),
                    stringResource(R.string.pref_board_font_size_small),
                    stringResource(R.string.pref_board_font_size_medium),
                    stringResource(R.string.pref_board_font_size_large)
                ),
                selected = fontSizeFactor,
                onSelect = { index ->
                    viewModel.updateFontSize(index)
                },
                onDismiss = { fontSizeDialog = false }
            )
        }
    }
}
