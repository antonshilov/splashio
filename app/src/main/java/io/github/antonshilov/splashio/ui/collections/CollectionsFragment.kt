import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import io.github.antonshilov.domain.feed.collections.Collection
import io.github.antonshilov.splashio.R
import io.github.antonshilov.splashio.ui.collections.CollectionListViewModel
import io.github.antonshilov.splashio.ui.collections.CollectionsAdapter
import kotlinx.android.synthetic.main.fragment_collections.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class CollectionsFragment : Fragment() {
  private val vm by viewModel<CollectionListViewModel>()
  private val adapter: CollectionsAdapter by inject()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_collections, container, false)
  }

  override fun onStart() {
    super.onStart()
    collections.adapter = adapter
    vm.collectionsList.observe(this, Observer<PagedList<Collection>> { if (it != null) handleCollection(it) })
  }

  private fun handleCollection(collections: PagedList<Collection>) {
    adapter.submitList(collections)
  }
}
